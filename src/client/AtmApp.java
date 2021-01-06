/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import org.json.JSONException;
import validation.ApacheValidator;
import validation.AtmArgs;
import validation.AtmException;

import java.io.IOException;

/**
 * @author Arkajit
 */
public class AtmApp {

    public static void main(String[] args) {

        // Validate args
        AtmArgs atmArgs = ApacheValidator.validateAtmArgs(args);
        if (!atmArgs.isValid()) {
            System.exit(255);
        }

        String account = atmArgs.getAccountName();
        String cardFile = atmArgs.getCardFile();
        String authFile = atmArgs.getAuthFile();

        AtmAccount atmAccount = null;
        // Validate auth file
        try {
            atmAccount = new AtmAccount(account, cardFile);
        } catch (IOException e) {
            System.exit(255);
        }

        String ip = atmArgs.getIp();
        int port = atmArgs.getPort();

        AtmClient client = new AtmClient(ip, port,authFile);

        TransactionRequest transactionRequest = null;
        double amount = atmArgs.getAmount();
        AtmArgs.ActionCode operation = atmArgs.getActionCode();

        // Create a request for connecting to server
        switch (operation) {
            case NEW:
                transactionRequest = new CreationRequest(atmAccount, client, amount);
                break;
            case DEPOSIT:
                transactionRequest = new DepositRequest(atmAccount, client, amount);
                break;
            case WITHDRAW:
                transactionRequest = new WithdrawRequest(atmAccount, client, amount);
                break;
            case BALANCE:
                transactionRequest = new BalanceRequest(atmAccount, client);
                break;
            default:
                System.exit(255);
                break;
        }

        // Connect to bank
        try {
            transactionRequest.doRequest();
        } catch (AtmException | JSONException e) {
            System.exit(255);
        } catch ( IOException  e) {
            // Exception while communicating
            System.exit(63);
        }

    }

}
   
