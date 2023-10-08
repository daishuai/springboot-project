package com.daishuai.websocket.server.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * 〈数据加解密工具类〉
 *
 * @author Daishuai
 * @createTime  2023/2/16
 */
public class AesEncryptUtils {

    /**
     * 密钥的默认位长度
     */
    public static final int DEFAULT_KEY_SIZE = 128;
    /**
     * 密钥算法类型
     */
    public static final String KEY_ALGORITHM = "AES";
    /**
     * 参数分别代表 算法名称/加密模式/数据填充方式
     */
    private static final String AES_ECB_PKCS_5_PADDING = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     *
     * @param content
     *            加密的字符串
     * @param encryptKey
     *            密钥
     * @return 加密后字符串
     * @throws Exception
     *             异常
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ECB_PKCS_5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM));
        byte[] b = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(b);
    }

    /**
     * 解密
     *
     * @param encryptStr
     *            解密的字符串
     * @param decryptKey
     *            密钥
     * @return 解密后的字符串
     * @throws Exception
     *             异常
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ECB_PKCS_5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM));
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * 生成 Hex 格式默认长度的随机密钥 字符串长度为 32，解二进制后为 16 个字节
     *
     * @return String Hex 格式的随机密钥
     */
    public static String initHexKey() {
        return Hex.encodeHexString(initKey());
    }

    /**
     * 生成默认长度的随机密钥 默认长度为 128
     *
     * @return byte[] 二进制密钥
     */
    public static byte[] initKey() {
        return initKey(DEFAULT_KEY_SIZE);
    }

    /**
     * 生成密钥 128、192、256 可选
     *
     * @param keySize
     *            密钥长度
     * @return byte[] 二进制密钥
     */
    public static byte[] initKey(int keySize) {
        // 实例化
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("no such algorithm exception: " + KEY_ALGORITHM, e);
        }
        keyGenerator.init(keySize);
        // 生成秘密密钥
        SecretKey secretKey = keyGenerator.generateKey();
        // 获得密钥的二进制编码形式
        return secretKey.getEncoded();
    }

}
