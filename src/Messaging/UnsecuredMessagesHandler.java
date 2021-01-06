package Messaging;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UnsecuredMessagesHandler extends AbstractMessagesHandler {

    public UnsecuredMessagesHandler(DataInputStream is, DataOutputStream os) {
        super(is, os);
    }

    @Override
    public void sendMessage(String message) throws IOException {
        this.sendMessageInBytes(message.getBytes());
    }

    public void sendMessageInBytes(byte[] byteMessage) throws IOException {
        try {
            int messageLength = byteMessage.length;
            os.writeInt(messageLength);
            os.write(byteMessage);
            os.flush();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String receiveMessage() throws IOException {
        byte[] message = this.receiveMessageInBytes();
        return new String(message);
    }

    public byte[] receiveMessageInBytes() throws IOException {
        int messageLength;
        try {
            messageLength = is.readInt();
        } catch (IOException e) {
            throw new IOException("Invalid message .");
        }
        if (messageLength <= 0 || messageLength > 1024 * 1024 * 10)
            throw new IOException("Invalid message length read.");

        byte[] message = new byte[messageLength];
        is.readFully(message, 0, messageLength);
        return message;

    }
}
