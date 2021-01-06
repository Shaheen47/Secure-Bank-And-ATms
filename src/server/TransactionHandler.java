package server;

import Security.MessageIntegretyManager;
import Security.SecurityUtilites;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Fakhr shaheen
 */
public abstract class TransactionHandler {
    protected Bank bank;
    protected JSONObject request;

    public TransactionHandler(Bank bank, JSONObject request) {
        this.bank = bank;
        this.request = request;
    }

    public JSONObject handleRequest() {


        boolean messageIsSafe = MessageIntegretyManager.checkAtmMessageIntegrety(request,bank.getAuthFile().getAuthHash());

        if (messageIsSafe) {
            JSONObject response;
            try {
                response = handleTransaction(request);
            } catch (JSONException e) {
                return replyProtocolErrorError();
            }

            try {
                //add hashing for message integrity
                String messageHash = MessageIntegretyManager.getHashedBankMessage(response,bank.getAuthFile().getAuthHash());
                response.put("hash", messageHash);
            } catch (JSONException e) {
                //
            }

            return response;
        } else {
            return replyProtocolErrorError();
        }
    }


    abstract protected JSONObject handleTransaction(JSONObject request) throws JSONException;


    protected JSONObject replyProtocolErrorError() {
        try {
            JSONObject js = new JSONObject();
            js.put("protocol_error", "");
            return js;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
