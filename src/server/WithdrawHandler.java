package server;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Fakhr shaheen
 */
public class WithdrawHandler extends TransactionHandler {


    public WithdrawHandler(Bank bank, JSONObject request) {
        super(bank, request);
    }

    @Override
    protected JSONObject handleTransaction(JSONObject request) throws JSONException {
        String acc = (String) request.get("account");
        double withdrawAmount;
        try {
            withdrawAmount = (double) request.get("withdraw");
        }catch (Exception e)
        {
            withdrawAmount = (int) request.get("withdraw");
        }

        String cardFile = (String) request.get("card-file");
        
        Account a = bank.findAccount(acc);
        JSONObject js = new JSONObject();
        js.put("account", acc);
        if ((a != null) && ((a.getCardFile().equals(cardFile)) ||(cardFile.isEmpty()))) {
            if (a.transaction(TransactionType.WITHDRAW,withdrawAmount)) {
                js.put("withdraw", withdrawAmount);
            } else
                js.put("error", "255");

        } else
            js.put("error", "255");
        return js;
    }

}
