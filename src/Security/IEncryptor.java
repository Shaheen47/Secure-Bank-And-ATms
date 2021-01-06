package Security;


import java.io.IOException;


/**
 *
 * @author Fakhr shaheen
 */
public interface IEncryptor {
     byte[] encrypt(byte[] message) throws IOException;
     byte[] decrypt(byte[] message) throws IOException;
}
