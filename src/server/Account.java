package server;

/**
 *
 * @author Fakhr shaheen
 */
public class Account {
    private String accountName;
    private double balance;

    private String cardFile;

    public Account(String accountName,double initialBalance,String cardFile)
    {
        this.accountName=accountName;
        this.balance=initialBalance;
        this.cardFile=cardFile;

    }

    public double getBalance() {

        return this.balance;

    }

    public String getAccountName() {

        return this.accountName;
    }
    public String getCardFile()
    {
        return this.cardFile;
    }

    private boolean withdraw(double  requiredAmount) {

        if ((this.balance >= requiredAmount) && (requiredAmount > 0)) {
            this.balance = this.balance-requiredAmount;
            this.balance = Double.parseDouble(String.format("%.2f", this.balance)); 
            return true;
        } else
            return false;
    }

    private boolean deposit(double  newDeposit) {

        if ((newDeposit>0) ) {
            this.balance = this.balance+newDeposit;
            this.balance = Double.parseDouble(String.format("%.2f", this.balance));
            return true;
        } else
            return false;
    }

    public synchronized boolean transaction(TransactionType transactionType,double amount)
    {
        boolean result;
        if(transactionType==TransactionType.DESPOSIT)
        {
            result=this.deposit(amount);
        }
        else
        {
            result=this.withdraw(amount);
        }
        return result;
    }
}
