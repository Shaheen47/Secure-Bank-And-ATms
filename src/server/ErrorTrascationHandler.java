package server;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * @author Fakhr shaheen
 */
public class ErrorTrascationHandler extends TransactionHandler {

    public ErrorTrascationHandler(Bank bank, JSONObject request) {
        super(bank, request);
    }

    @Override
    public JSONObject handleRequest() {
        return replyProtocolErrorError();
    }

    @Override
    protected JSONObject handleTransaction(JSONObject request) throws JSONException {
        return replyProtocolErrorError();
    }
}


