package client;

import Security.MessageIntegretyManager;
import Security.SecurityUtilites;
import org.json.JSONException;
import org.json.JSONObject;
import validation.AtmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;


/**
 * @author Fakhr Shaheen
 */
public abstract class TransactionRequest {

    protected AtmAccount atmAccount;
    private AtmClient client;

    TransactionRequest(AtmAccount atmAccount, AtmClient client) {
        this.atmAccount = atmAccount;
        this.client = client;
    }

    final public void doRequest() throws AtmException, JSONException, IOException {

        doBeforeSending();

        JSONObject message = createRequestMainPart(atmAccount);
        message = createRequest(message);

        String hash = MessageIntegretyManager.getHashedAtmMessage(message,client.getAuthFile().getAuthHash());
        message.put("hash", hash);
        String stringMessage = message.toString();

        // Connect
        client.sendMessage(stringMessage);
        String receivedMessage = client.receiveMessage();
        client.close();

        JSONObject receivedMessageJson = new JSONObject(receivedMessage);
        boolean messageIsSafe = checkMessageSafty(receivedMessageJson);

        if (messageIsSafe) {
            if(!hasErrors(receivedMessageJson)) {
                printLog(receivedMessageJson);
                doAfterRespone();
            }
            else
                throw new AtmException();
        }
        else
            throw new IOException();

    }

    abstract protected JSONObject createRequest(JSONObject message) throws JSONException;

    protected void doAfterRespone() {
        ;
    }

    abstract void doBeforeSending() throws AtmException;

    private JSONObject createRequestMainPart(AtmAccount atmAccount) throws JSONException {
        JSONObject js = new JSONObject();
        js.put("account", atmAccount.getAccountName());
        js.put("card-file", atmAccount.getCardFile());

        return js;
    }

    private boolean hasErrors(JSONObject message) {
        return message.has("error");
    }

    private boolean checkMessageSafty(JSONObject jsonReceivedMessage) throws JSONException {
        //check message integrety
        boolean messageIsNotManipulated = MessageIntegretyManager.checkBankMessageIntegrety(jsonReceivedMessage,client.getAuthFile().getAuthHash());
        return messageIsNotManipulated;
    }

    private void printLog(JSONObject message) throws JSONException {
        JSONObject msg = new JSONObject(message.toString());
        msg.remove("hash");
        System.out.println(msg.toString());
        System.out.flush();
    }
}
