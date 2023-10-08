package com.daishuai.websocket.server.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;

/**
 * @author daishuai
 * @version 1.0.0
 * @description RSA算法工具
 * @createTime 2023年10月08日 14:22:00
 */
public class RSAUtil {

    private static final KeyPair keyPair = initKey();
    private static KeyPair initKey() {
        try {
            Provider provider =new BouncyCastleProvider();
            Security.addProvider(provider);
            SecureRandom random = new SecureRandom();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", provider);
            generator.initialize(1024,random);
            return generator.generateKeyPair();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] encrypt(String plainText) throws Exception {
        Provider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        return cipher.doFinal(plainText.getBytes());
    }

    public static String encryptBase64(String plainText) throws Exception {
        return Base64.encodeBase64String(encrypt(plainText));
    }
    private static byte[] decrypt(byte[] byteArray) {
        try {
            Provider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
            PrivateKey privateKey = keyPair.getPrivate();
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(byteArray);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String decryptBase64(String string) {
        return new String(decrypt(Base64.decodeBase64(string.getBytes())));
    }
    public static String generateBase64PublicKey() {
        PublicKey publicKey = keyPair.getPublic();
        return new String(Base64.encodeBase64(publicKey.getEncoded()));
    }
}
