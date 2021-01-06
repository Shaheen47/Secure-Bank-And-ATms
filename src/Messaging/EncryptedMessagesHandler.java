package Messaging;

import Security.AesEnctyptor;
import Security.IEncryptor;
import Security.IKeyExchanger;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * @author Fakhr shaheen
 */
public class EncryptedMessagesHandler extends AbstractMessagesHandler {
    IEncryptor encryptor;

    public EncryptedMessagesHandler(DataInputStream is, DataOutputStream os, IKeyExchanger keyExchanger) throws IOException {
        super(is, os);
        byte[] key = keyExchanger.exchangeKeys(os, is);
        encryptor = new AesEnctyptor(key);
    }

    public EncryptedMessagesHandler(DataInputStream is, DataOutputStream os, IKeyExchanger keyExchanger,byte[] staticKey) throws IOException {
        super(is, os);
        byte[] key = keyExchanger.exchangeKeys(os, is);
        encryptor = new AesEnctyptor(key,staticKey);
    }

    @Override
    public void sendMessage(String message) throws IOException {

        byte[] byteMessage = message.getBytes();
        byteMessage = encryptor.encrypt(byteMessage);
        int messageLength = byteMessage.length;
        os.writeInt(messageLength);
        os.write(byteMessage);
        os.flush();
    }

    @Override
    public String receiveMessage() throws IOException {

        int messageLength = is.readInt();
        if (messageLength <= 0 || messageLength>1024*1024*10)
            throw new IOException("Invalid message length read.");
        byte[] encryptedMessage = new byte[messageLength];
        is.readFully(encryptedMessage, 0, messageLength);
        byte[] decryptedMessage = encryptor.decrypt(encryptedMessage);

        return new String(decryptedMessage);
    }

}
