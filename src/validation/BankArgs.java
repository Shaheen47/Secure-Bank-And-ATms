package validation;

public class BankArgs {
    private boolean valid;
    private int returnCode;
    private String authFile;
    private int port;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getAuthFile() {
        return authFile;
    }

    public void setAuthFile(String authFile) {
        this.authFile = authFile;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "BankArgs{" +
                "valid=" + valid +
                ", returnCode=" + returnCode +
                ", authFile='" + authFile + '\'' +
                ", port=" + port +
                '}';
    }
}
