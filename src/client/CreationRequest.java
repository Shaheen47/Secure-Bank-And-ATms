package client;

import org.json.JSONException;
import org.json.JSONObject;
import validation.AtmException;


/**
 * @author Fakhr Shaheen
 */
public class CreationRequest extends TransactionRequest {
    private double balance;

    CreationRequest(AtmAccount atmAccount, AtmClient client, double balance)
    {
        super(atmAccount, client);
        this.balance=balance;
    }

    @Override
    protected void doBeforeSending() throws AtmException {
        if(atmAccount.cardExists())
        {
            throw new AtmException();
        }
    }

    @Override
    protected JSONObject createRequest(JSONObject message) throws JSONException {
        if((this.balance>=10)) {
        message.put("initial_balance",this.balance);
        return message;
        }
        else{
            System.exit(255);
        }
        return null;
    }

    @Override
    protected void doAfterRespone() {
       atmAccount.createCard();
    }
}
