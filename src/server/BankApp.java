package server;

import validation.ApacheValidator;
import validation.BankArgs;

import java.io.IOException;

/**
 * @author Fakhr shaheen
 */
public class BankApp {
    private BankServer bankServer;

    public static void main(String[] args) {
        BankApp bankApp = new BankApp();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                if (bankApp.bankServer != null) {
                    bankApp.bankServer.shutdown();
                }
            }
        });

        BankArgs bankArgs = ApacheValidator.validateBankArgs(args);
        if (bankArgs.isValid()) {
            String authFile = bankArgs.getAuthFile();
            int port = bankArgs.getPort();

            try {
                bankApp.bankServer = new BankServer(authFile, port);
            } catch (IOException e) {
                System.exit(255);
            }

            bankApp.bankServer.run();
        } else {
            System.exit(255);
        }

    }


}
