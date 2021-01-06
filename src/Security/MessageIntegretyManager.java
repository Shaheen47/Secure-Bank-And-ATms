package Security;

import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Fakhr shaheen
 */
public class MessageIntegretyManager {

    public static boolean checkAtmMessageIntegrety(JSONObject jsonObject,String auth) {
        try {

            if (getHashedAtmMessage(jsonObject,auth).equals(getMessageHash(jsonObject)))
                return true;
            else
                return false;
        }catch (Exception e)
        {
            return false;
        }
    }



    public static String getHashedAtmMessage(JSONObject jsonObject,String auth) throws JSONException {
        String account = (String) jsonObject.get("account");
        String cardFile = (String) jsonObject.get("card-file");
        String mainPart = account + cardFile + auth;
        String operationPart=messageOperation(jsonObject);
        String message=mainPart+operationPart;
        String hashedMessage=SecurityUtilites.sha256(message);
        return hashedMessage;

    }



    public static boolean checkBankMessageIntegrety(JSONObject jsonObject,String auth) throws JSONException {
        if(getHashedBankMessage(jsonObject,auth).equals(getMessageHash(jsonObject)))
            return true;
        else
            return false;
    }


    public static String getHashedBankMessage(JSONObject jsonObject,String auth) throws JSONException {
        String account = (String) jsonObject.get("account");
        String mainPart =account+auth;
        String message=mainPart+messageOperation(jsonObject);
        String hashedMessage=SecurityUtilites.sha256(message);
        return hashedMessage;

    }


    private static String messageOperation(JSONObject x) throws JSONException {
        String operation;
        if (x.has("balance")) {
            operation ="balance";
        }
        else if (x.has("initial_balance")) {
            double bal;
            try {
                bal = (double) x.get("initial_balance");
            }catch (Exception e)
            {
                bal = (int) x.get("initial_balance");
            }

            operation ="initial_balance" + bal;
        } else if (x.has("deposit"))
        {
            double deposit;
            try {
                deposit = (double) x.get("deposit");
            }catch (Exception e)
            {
                deposit = (int) x.get("deposit");
            }

            operation ="deposit" + deposit;
        }
        else if (x.has("withdraw")) {
            double withdraw;
            try {
                withdraw = (double) x.get("withdraw");
            }catch (Exception e)
            {
                withdraw = (int) x.get("withdraw");
            }

            operation ="withdraw" + withdraw;
        }
        else
            operation=null;
        return operation;
    }

    private static String getMessageHash(JSONObject jsonObject) throws JSONException {
        String hash = (String) jsonObject.get("hash");
        return hash;

    }

}
