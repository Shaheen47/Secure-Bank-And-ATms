package client;

import org.json.JSONException;
import org.json.JSONObject;
import validation.AtmException;


/**
 * @author Fakhr Shaheen
 */
public class DepositRequest extends TransactionRequest {
    private double  balance;

    DepositRequest(AtmAccount atmAccount, AtmClient client, double balance) {
        super(atmAccount, client);
        this.balance = balance;
    }

    @Override
    protected JSONObject createRequest(JSONObject message) throws JSONException {

        message.put("deposit", balance);
        return message;
    }

    @Override
    void doBeforeSending() throws AtmException {
        if (!atmAccount.cardExists()) {
            throw new AtmException();
        }
    }
}