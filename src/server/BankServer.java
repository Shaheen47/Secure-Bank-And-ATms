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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 * @author Fakhr shaheen
 */
public class BankServer {

    private Bank bank;
    private ServerSocket serverSocker;
    private final static int timeout = 10000;

    public BankServer(String authFilename, int port) throws IOException {
        this.serverSocker = new ServerSocket(port);
        bank = new Bank(authFilename);
    }


    public void run() {
        while (true) {
            try {
                Socket socket;
                socket = serverSocker.accept();
                socket.setSoTimeout(timeout);
                RequestHandler requestHandler = new RequestHandler(bank, socket);
                requestHandler.start();
            } catch (IOException e) {
                continue;
            }

        }
    }

    public void shutdown() {
        if (this.serverSocker != null) {
            try {
                this.serverSocker.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
        bank.getAuthFile().deleteAuthFile();
    }
}
