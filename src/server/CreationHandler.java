package server;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Fakhr shaheen
 */
public class CreationHandler extends TransactionHandler {


    public CreationHandler(Bank bank, JSONObject request) {
        super(bank, request);
    }

    @Override
    protected JSONObject handleTransaction(JSONObject request) throws JSONException {
        String acc = (String) request.get("account");
        String cardFile = (String) request.get("card-file");
        double balance;
        try {
            balance = (double) request.get("initial_balance");
        }catch (Exception e)
        {
            balance = (int) request.get("initial_balance");
        }
        Account a = new Account(acc, balance,cardFile);
        JSONObject js = new JSONObject();
        js.put("account", acc);
        if (bank.createAccount(a)) {
            js.put("initial_balance", balance);
        } else
            js.put("error", "255");
        return js;
    }
}
