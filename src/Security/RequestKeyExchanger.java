package Security;

import Messaging.UnsecuredMessagesHandler;

import javax.crypto.KeyAgreement;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class RequestKeyExchanger implements IKeyExchanger {
    @Override
    public byte[] exchangeKeys(DataOutputStream os, DataInputStream is) throws IOException{
        try {

            UnsecuredMessagesHandler messagesHandler=new UnsecuredMessagesHandler(is,os);
            /*
             * server creates its own DH key pair with 2048-bit key size
             */
            KeyPairGenerator serverKpairGen = KeyPairGenerator.getInstance("DH");
            serverKpairGen.initialize(2048);
            KeyPair serverKpair = serverKpairGen.generateKeyPair();

            // server creates and initializes her DH KeyAgreement object
            KeyAgreement serverKeyAgree = KeyAgreement.getInstance("DH");
            serverKeyAgree.init(serverKpair.getPrivate());

            // server encodes its public key, and sends it over to client.
            byte[] serverPubKeyEnc = serverKpair.getPublic().getEncoded();

            messagesHandler.sendMessageInBytes(serverPubKeyEnc);

            //reading the response
            byte[] clientPubKeyEnc=messagesHandler.receiveMessageInBytes();

            /*
             * server uses client's public key for the first (and only) phase
             * of its version of the DH
             * protocol.
             * Before it can do so, it has to instantiate a DH public key
             * from client's encoded key material.
             */
            KeyFactory serverKeyFac = KeyFactory.getInstance("DH");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(clientPubKeyEnc);
            PublicKey clientPubKey = serverKeyFac.generatePublic(x509KeySpec);
            serverKeyAgree.doPhase(clientPubKey, true);

            byte[] serverSharedSecret = serverKeyAgree.generateSecret();
            return serverSharedSecret;

        } catch (NoSuchAlgorithmException|InvalidKeyException|InvalidKeySpecException e) {
            return null;
        }

    }
}
