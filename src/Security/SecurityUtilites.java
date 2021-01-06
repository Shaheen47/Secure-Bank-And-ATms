package Security;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * @author Fakhr shaheen
 */
public class SecurityUtilites {

    private static final int DEFAULT_AUTH_LEN = 128;

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
            return "";
        }
    }

    public static byte[] generateAuth(int length) {
        SecureRandom rnd = new SecureRandom();
        byte[] token = new byte[length];
        rnd.nextBytes(token);

        return token;
    }



    public static int getDefaultAuthLen() {
        return DEFAULT_AUTH_LEN;
    }


}
