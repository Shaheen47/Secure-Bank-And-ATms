package Security;

import javax.crypto.*;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 *
 * @author Fakhr shaheen
 */
public class AesEnctyptor implements IEncryptor {
    private Cipher encCipher;
    private Cipher decCipher;
    SecretKeySpec keySpec;

    public AesEnctyptor(byte[] key) throws IOException {
        try {

            encCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            keySpec = new SecretKeySpec(key,0,16, "AES");

            byte[] encodedParams = encCipher.getParameters().getEncoded();
            AlgorithmParameters aesParams = AlgorithmParameters.getInstance("AES");
            aesParams.init(encodedParams);
            byte[] iv = generateIV(16, key);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            encCipher.init(Cipher.ENCRYPT_MODE, keySpec,ivspec);
            decCipher.init(Cipher.DECRYPT_MODE, keySpec,ivspec);

        } catch (NoSuchAlgorithmException|NoSuchPaddingException|InvalidAlgorithmParameterException|InvalidKeyException e) {
            //Do something
        }
    }

    public AesEnctyptor(byte[] key,byte[] staticKey) throws IOException {
        try {

            byte[] combined = new byte[key.length + staticKey.length];
            encCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            keySpec = new SecretKeySpec(combined,0,32, "AES");

            byte[] encodedParams = encCipher.getParameters().getEncoded();
            AlgorithmParameters aesParams = AlgorithmParameters.getInstance("AES");
            aesParams.init(encodedParams);
            byte[] iv = generateIV(16, combined);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            encCipher.init(Cipher.ENCRYPT_MODE, keySpec,ivspec);
            decCipher.init(Cipher.DECRYPT_MODE, keySpec,ivspec);

        } catch (NoSuchAlgorithmException|NoSuchPaddingException|InvalidAlgorithmParameterException|InvalidKeyException e) {
            //Do something
        }
    }

    @Override
    public byte[] encrypt(byte[] message) {
        try {
            return encCipher.doFinal(message);
        } catch (BadPaddingException|IllegalBlockSizeException e) {
            //ToDo
            System.exit(1);
            return null;
        }
    }

    @Override
    public byte[] decrypt(byte[] message)  {
        try {
            return decCipher.doFinal(message);
        } catch (BadPaddingException|IllegalBlockSizeException e) {
            System.exit(1);
            return null;
        }
    }

    private byte[] generateIV(int length, byte[] base) {
        int baseLength = base.length - 1;
        byte[] iv = new byte[length];

        for(int i = 0; i < length; i++)
            iv[i] = base [baseLength - i];

        return iv;
    }
}

