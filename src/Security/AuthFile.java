package Security;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Fakhr shaheen
 */

public class AuthFile {

    private String authFileName;
    private String authHash;
    private byte[] encryptionKey;

    public AuthFile(String authFileName) throws IOException {
        this.authFileName=authFileName;
        File file = new File(authFileName);
        if (file.exists()) {
            throw new IOException();
        }

        byte[] authToken = SecurityUtilites.generateAuth(SecurityUtilites.getDefaultAuthLen());
        this.authHash = SecurityUtilites.sha256(new String(authToken));
        encryptionKey = SecurityUtilites.generateAuth(SecurityUtilites.getDefaultAuthLen());
        FileWriter f = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(f);
        bufferedWriter.write(authHash);
        bufferedWriter.newLine();
        bufferedWriter.write(encryptionKey.toString());
        bufferedWriter.close();
        f.close();

        System.out.println("created");
        System.out.flush();
    }

    public AuthFile(String authFileName,String authHash,byte[] encryptionKey)
    {
        this.authFileName=authFileName;
        this.authHash=authHash;
        this.encryptionKey=encryptionKey;
    }

    public static AuthFile openAuthFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            String authFileName=path;
            String authHash;
            byte[] encryptionKey;
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            authHash=bufferedReader.readLine();
            encryptionKey=bufferedReader.readLine().getBytes();
            bufferedReader.close();
            fileReader.close();
            return new AuthFile(authFileName,authHash,encryptionKey);
        } else {
            throw new IOException();
        }
    }

    public boolean deleteAuthFile() {
        File file = new File(this.authFileName);
        if (file.exists() && !file.isDirectory())
            return file.delete();
        else
            return false;
    }

    public String getAuthHash() {
        return authHash;
    }

    public byte[] getEncryptionKey() {
        return encryptionKey;
    }
}
