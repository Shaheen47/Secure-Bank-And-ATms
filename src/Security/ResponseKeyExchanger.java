package Security;

import Messaging.UnsecuredMessagesHandler;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class ResponseKeyExchanger implements IKeyExchanger {
    @Override
    public byte[] exchangeKeys(DataOutputStream os, DataInputStream is) throws IOException {

        try {

            UnsecuredMessagesHandler messagesHandler = new UnsecuredMessagesHandler(is, os);
            /*
             * client has received server's public key
             * in encoded format.
             * it instantiates a DH public key from the encoded key material.
             */
            byte[] serverPubKeyEnc = messagesHandler.receiveMessageInBytes();

            KeyFactory clientKeyFac = KeyFactory.getInstance("DH");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(serverPubKeyEnc);

            PublicKey serverPubKey = clientKeyFac.generatePublic(x509KeySpec);

            /*
             * client gets the DH parameters associated with server's public key.
             * it must use the same parameters when it generates its own key
             * pair.
             */
            DHParameterSpec dhParamFromAlicePubKey = ((DHPublicKey) serverPubKey).getParams();

            // client creates its own DH key pair
            KeyPairGenerator clientKpairGen = KeyPairGenerator.getInstance("DH");
            clientKpairGen.initialize(dhParamFromAlicePubKey);
            KeyPair clientKpair = clientKpairGen.generateKeyPair();

            // client creates and initializes his DH KeyAgreement object
            KeyAgreement clientKeyAgree = KeyAgreement.getInstance("DH");
            clientKeyAgree.init(clientKpair.getPrivate());

            // client encodes its public key, and sends it over to server.
            byte[] clientPubKeyEnc = clientKpair.getPublic().getEncoded();

            messagesHandler.sendMessageInBytes(clientPubKeyEnc);

            clientKeyAgree.doPhase(serverPubKey, true);

            byte[] clientSharedSecret = clientKeyAgree.generateSecret();

            return clientSharedSecret;
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
            return null;
        }
    }
}
