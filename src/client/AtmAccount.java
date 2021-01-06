package client;

import Security.AuthFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static Security.AuthFile.openAuthFile;


/**
 * @author Arkajit
 */

public class AtmAccount {

    private String cardFile;
    private String accountName;

    public AtmAccount(String accountName,String cardFile) throws IOException {
        this.accountName = accountName;
        this.cardFile=cardFile;
    }


    public String getCardFile()
    {
        return this.cardFile;
    }

    public String getAccountName() {
        return accountName;
    }

    public void createCard()  {

        try {
            File f = new File(cardFile);
            f.createNewFile();
        }
        catch (Exception e)
        {
            ;
        }

    }

    public boolean cardExists() {

        if (this.cardFile.isEmpty()){
            return false;
        }
        else{
        File f = new File(this.cardFile);
        return f.exists() && !f.isDirectory();
        }
    }



}
