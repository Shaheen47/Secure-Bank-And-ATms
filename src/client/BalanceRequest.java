package client;

import org.json.JSONException;
import org.json.JSONObject;
import validation.AtmException;


/**
 * @author Fakhr Shaheen
 */
public class BalanceRequest extends TransactionRequest {

    BalanceRequest(AtmAccount atmAccount, AtmClient client) {
        super(atmAccount, client);
    }

    @Override
    protected JSONObject createRequest(JSONObject message) throws JSONException {
        message.put("balance", "");
        return message;
    }

    @Override
    void doBeforeSending() throws AtmException {
        if (!atmAccount.cardExists()) {
            throw new AtmException();
        }
    }
}
