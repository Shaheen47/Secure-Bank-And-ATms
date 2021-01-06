package client;

import Messaging.AbstractMessagesHandler;
import Messaging.EncryptedMessagesHandler;


import Security.AuthFile;
import Security.IKeyExchanger;
import Security.ResponseKeyExchanger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class AtmClient {

    private Socket client;
    private String host;
    private int port;
    private AuthFile authFile;
    private AbstractMessagesHandler messagesHandler;
    int MAX_SECONDS = 10000;

    public AtmClient(String host, int port,String authFilePath) {
        this.host = host;
        this.port = port;
        try {
            this.authFile = AuthFile.openAuthFile(authFilePath);
        }catch (IOException e)
        {
            System.exit(63);
        }
    }

    private void init() throws IOException {
        try {
            client = new Socket();
            client.connect(new InetSocketAddress(host, port), MAX_SECONDS);
            client.setSoTimeout(MAX_SECONDS);

            DataOutputStream os = new DataOutputStream(client.getOutputStream());
            DataInputStream is = new DataInputStream(client.getInputStream());

            IKeyExchanger keyExchanger=new ResponseKeyExchanger();
            messagesHandler = new EncryptedMessagesHandler(is, os, keyExchanger,authFile.getEncryptionKey());

        } catch (IOException e) {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    //
                }
            }
            throw new IOException();
        }
    }

    public void sendMessage(String message) throws IOException{
        if (this.client == null) {
            init();
        }

        this.messagesHandler.sendMessage(message);
    }

    public String receiveMessage() throws IOException {
        if (this.client == null) {
            init();
        }

        return this.messagesHandler.receiveMessage();
    }

    public AuthFile getAuthFile() {
        return authFile;
    }

    public void close() {
        try {
            if (this.client != null)
                this.client.close();
        } catch (IOException e) {
            // Do nothing
        }
    }


}
