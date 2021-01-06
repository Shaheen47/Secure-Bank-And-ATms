package validation;

public class AtmArgs {
    private boolean valid;
    private int returnCode;
    private String authFile;
    private String cardFile;
    private String accountName;
    private String ip;
    private int port;
    private ActionCode actionCode;
    private double amount;


    public enum ActionCode{
        NEW('n'),
        DEPOSIT('d'),
        WITHDRAW('w'),
        BALANCE('g');

        private final char code;

        ActionCode(char code) {
            this.code = code;
        }

        public ActionCode getFor(char code)
        {
            for(ActionCode actionCode : ActionCode.values())
            {
                if(actionCode.code == code)
                    return actionCode;
            }
            return null;
        }
        
        public char getValue(){
            return code;
        }
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public ActionCode getActionCode() {
        return actionCode;
    }

    public void setActionCode(ActionCode actionCode) {
        this.actionCode = actionCode;
    }

    public String getAuthFile() {
        return authFile;
    }

    public void setAuthFile(String authFile) {
        this.authFile = authFile;
    }

    public String getCardFile() {
        return cardFile;
    }

    public void setCardFile(String cardFile) {
        this.cardFile = cardFile;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ValidatorResponse{" +
                "valid=" + valid +
                ", returnCode=" + returnCode +
                ", authFile='" + authFile + '\'' +
                ", cardFile='" + cardFile + '\'' +
                ", accountName='" + accountName + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", actionCode=" + actionCode +
                ", amount=" + amount +
                '}';
    }
}
