package server;

import Security.AuthFile;
import Security.SecurityUtilites;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fakhr shaheen
 */
public class Bank {
    private List<Account> accounts;
    private String authFileName;
    private AuthFile authFile;

    public Bank(String authFileName) throws IOException {
        accounts = new ArrayList<>();
        this.authFileName = authFileName;
        this.authFile=new AuthFile(authFileName);
    }

    public boolean accountExists(Account account) {

        for (Account x : accounts) {

            if (x.getAccountName().equals(account.getAccountName())) {

                return true;
            }
        }
        return false;
    }

    public boolean createAccount(Account account) {
        if (account != null) {
            if (!accountExists(account)) {
                if ((account.getBalance() >= 10)) {
                    accounts.add(account);
                    return true;
                }
            }
        }
        return false;

    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Account findAccount(String accountName) {

        for (Account a : accounts) {

            if ((a.getAccountName().equals(accountName))) {
                return a;
            }
        }

        return null;

    }

    public AuthFile getAuthFile(){return authFile;}

}
