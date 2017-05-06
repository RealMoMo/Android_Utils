package com.realmo.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES加密解密
 */
public class DesUtil {

    private static final String ALGORITHM = "DES";
    private static final String KEY = "12345678";
    public static final String LOCAL_KEY = "12344321";//本地加密秘钥

    /**
     * 初始化生成密钥
     */
    private static String initKey(String seed) {
        try {
            SecureRandom random = null;
            if (seed != null) {
                random = new SecureRandom(seed.getBytes());
            } else {
                random = new SecureRandom();
            }
            // 初始化密钥生成器
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(random);
            SecretKey key = keyGenerator.generateKey();
            // 获取密钥二进制字节数组
            byte[] keyBytes = key.getEncoded();
            // 转换为Base64编码内容
            return Base64Util.encodeByte(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     */
    public static String encrypt(String src) {
        return encrypt(src, KEY);
    }

    /**
     * 加密 传入秘钥
     */
    public static String encrypt(String src, String keyString) {
        try {
            // 获取密钥 - Key应该是Base64编码后的密钥的字符串
            // 把字符串转换为密钥对象
            Key key = getKey(keyString);
            IvParameterSpec zeroIv = new IvParameterSpec(KEY.getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] bytes = cipher.doFinal(src.getBytes("UTF-8"));
            return Base64Util.encodeByte(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 解密
     */
    public static String decrypt(String dest){
        try {
            return decrypt(dest, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return dest.trim();
        }
    }

    public static String decrypt(String dest, String keyString) throws Exception {
        Key key = getKey(keyString);
        IvParameterSpec zeroIv = new IvParameterSpec(KEY.getBytes("UTF-8"));
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        // 把加密数据的Base64串进行解码，转换为字节数组
        byte[] bytes = Base64Util.decodeToBytes(dest);
        // 明文的字节数组
        byte[] decryptBytes = cipher.doFinal(bytes);
        return new String(decryptBytes, "UTF-8");
    }

    /**
     * 通过密钥字符串生成 密钥Key
     */
    private static Key getKey(String keyString) {
        try {
//            String key = Base64Util.decode(keyString);
            DESKeySpec keySpec = new DESKeySpec(keyString.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            Key secretKey = keyFactory.generateSecret(keySpec);
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
