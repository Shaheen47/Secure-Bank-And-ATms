package server;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class BalanceHandler extends TransactionHandler {


    public BalanceHandler(Bank bank, JSONObject request) {
        super(bank, request);
    }

    @Override
    protected JSONObject handleTransaction(JSONObject request) throws JSONException {
        String acc = (String) request.get("account");
        String cardFile = (String) request.get("card-file");
        Account a = bank.findAccount(acc);

        JSONObject js = new JSONObject();
        js.put("account", acc);
        if ((a != null) && ((a.getCardFile().equals(cardFile)) || (cardFile.isEmpty())) ) {
            double bal = a.getBalance();
            js.put("balance", bal);
        } else
            js.put("error", "255");
        return js;
    }
}
