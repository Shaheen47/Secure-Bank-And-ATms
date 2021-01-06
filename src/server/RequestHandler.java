package server;

import Messaging.AbstractMessagesHandler;
import Messaging.EncryptedMessagesHandler;
import Messaging.UnsecuredMessagesHandler;
import Security.IKeyExchanger;
import Security.RequestKeyExchanger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class RequestHandler extends Thread {

    private Socket socket;
    private Bank bank;

    public RequestHandler(Bank bank, Socket socket) {
        this.socket = socket;
        this.bank = bank;
    }

    @Override
    public void run() {
        try {
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());

            IKeyExchanger keyExchanger=new RequestKeyExchanger();
            AbstractMessagesHandler messagesHandler = new EncryptedMessagesHandler(is, os, keyExchanger,bank.getAuthFile().getEncryptionKey());

            String receivedMessage;
            try {
                receivedMessage = messagesHandler.receiveMessage();
            } catch (SocketTimeoutException e) {
                System.out.println("protocol_error");
                System.out.flush();
                return;
            }
            JSONObject jsonReceivedMessage = new JSONObject(receivedMessage);

            TransactionHandler transactionHandler;

            if (jsonReceivedMessage.has("balance"))
                transactionHandler = new BalanceHandler(bank, jsonReceivedMessage);
            else if (jsonReceivedMessage.has("initial_balance"))
                transactionHandler = new CreationHandler(bank, jsonReceivedMessage);
            else if (jsonReceivedMessage.has("deposit"))
                transactionHandler = new DepositHandler(bank, jsonReceivedMessage);
            else if (jsonReceivedMessage.has("withdraw"))
                transactionHandler = new WithdrawHandler(bank, jsonReceivedMessage);
            else
                transactionHandler = new ErrorTrascationHandler(bank, jsonReceivedMessage);


            JSONObject response = transactionHandler.handleRequest();

            if (response.has("protocol_error")) {
                System.out.println("protocol_error");
                System.out.flush();
                return;
            }
            //send
            else if (!response.has("error")) {

                JSONObject msg = new JSONObject(response.toString());
                msg.remove("hash");
                System.out.println(msg.toString());
            }
            messagesHandler.sendMessage(response.toString());

        } catch (IOException | JSONException e) {
            System.out.println("protocol_error");
            System.out.flush();
            return;
        }
        finally {
            try {
            socket.close();}
            catch (IOException e)
            {
                return;
            }
        }
    }
}
