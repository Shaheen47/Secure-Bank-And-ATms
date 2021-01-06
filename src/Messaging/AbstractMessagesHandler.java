package Messaging;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Fakhr shaheen
 */

public abstract class AbstractMessagesHandler {
    protected DataInputStream is;
    protected DataOutputStream os;

    public AbstractMessagesHandler(DataInputStream is, DataOutputStream os) {
        this.is = is;
        this.os = os;
    }

    public abstract void sendMessage(String message) throws IOException;

    public abstract String receiveMessage() throws IOException;
}
