package Security;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface IKeyExchanger {
    byte[]  exchangeKeys(DataOutputStream os, DataInputStream is) throws IOException;
}
