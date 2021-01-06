package server;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Fakhr shaheen
 */
public class DepositHandler extends TransactionHandler {


    public DepositHandler(Bank bank, JSONObject request) {
        super(bank, request);
    }

    @Override
    protected JSONObject handleTransaction(JSONObject request) throws JSONException {
        String acc = (String) request.get("account");
        double deposit;
        try {
            deposit = (double) request.get("deposit");
        }catch (Exception e)
        {
            deposit = (int) request.get("deposit");
        }

        String cardFile = (String) request.get("card-file");

        Account a = bank.findAccount(acc);
        JSONObject js = new JSONObject();
        js.put("account", acc);
        if ((a != null) && ((a.getCardFile().equals(cardFile)) ||(cardFile.isEmpty()))) {
            if (a.transaction(TransactionType.DESPOSIT,deposit)) {
                js.put("deposit", deposit);
            } else {
                js.put("error", "255");
            }
        } else
            js.put("error", "255");
        return js;
    }
}
