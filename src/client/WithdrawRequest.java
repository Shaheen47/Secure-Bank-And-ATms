package client;

import org.json.JSONException;
import org.json.JSONObject;
import validation.AtmException;


/**
 * @author Fakhr Shaheen
 */
public class WithdrawRequest extends TransactionRequest {

    private double balance;

    WithdrawRequest(AtmAccount atmAccount, AtmClient client, double balance) {
        super(atmAccount, client);
        this.balance = balance;
    }

    @Override
    protected JSONObject createRequest(JSONObject message) throws JSONException {
        
        message.put("withdraw", balance);
        return message;
    }

    @Override
    void doBeforeSending() throws AtmException {
        if (!atmAccount.cardExists()) {
            throw new AtmException();
        }
    }
}

