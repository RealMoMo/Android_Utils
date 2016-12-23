package momo.com.day45_encrption;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by realmo on 2016/12/9 0009.
 */
public class EncryptionHelper {


    //两个大素数的乘积
    private static final String MODULUS = "100631058000714094813874361191853577129731636346684218206605779824931626830750623070803100189781211343851763275329364056640619755337779928985272486091431384128027213365372009648233171894708338213168824861061809490615593530405056055952622249066180336803996949444124622212096805545953751253607916170340397933039";
    //公钥
    private static final String PUB_KEY = "65537";
    //私钥
    private static final String PRI_KEY = "26900155715313643087786516528374548998821559381075740707715132776187148793016466508650068087107695523642202737697714709374658856733792614490943874205956727606674634563665154616758939576547663715234643273055658829482813503959459653708062875625210008961239643775661357655599312857249418610810177817213648575161";


    public static final int ENCODE = 0;
    public static final int DECODE = 1;


    /**
     * 使用DES算法完成对称加密
     *
     * @param content 明文
     * @param key     密钥
     * @param type    加密还是解密
     * @return
     */
    public static String des(String content,String key,int type){
        String charset = "UTF-8";
        try {
            byte[] keyBytes = key.getBytes(charset);
            byte[] temp = new byte[8];
            System.arraycopy(keyBytes,0,temp,0,Math.min(temp.length,keyBytes.length));
            //获取密钥对象
            Key keySpce = new SecretKeySpec(temp,"des");
            //获取密码生成器
            Cipher cipher = Cipher.getInstance("des");
            //加密
            if(type == ENCODE){
                //初始化密码生成器
                cipher.init(Cipher.ENCRYPT_MODE,keySpce);
                //加密
                byte[] bytes = cipher.doFinal(content.getBytes(charset));
                return Base64.encodeToString(bytes,Base64.DEFAULT);
            }else if(type == DECODE){
                cipher.init(Cipher.DECRYPT_MODE,keySpce);
                byte[] bytes = cipher.doFinal(Base64.decode(content, Base64.DEFAULT));
                return new String(bytes,0,bytes.length);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }


        return  null;
    }


    public static String des3(String content,String key,int type){
        String charset = "UTF-8";
        try {
            byte[] keyBytes = key.getBytes(charset);
            byte[] temp = new byte[24];
            System.arraycopy(keyBytes,0,temp,0,Math.min(temp.length,keyBytes.length));
            //获取密钥对象
            Key keySpce = new SecretKeySpec(temp,"desede");
            //获取密码生成器
            Cipher cipher = Cipher.getInstance("desede");
            //加密
            if(type == ENCODE){
                //初始化密码生成器
                cipher.init(Cipher.ENCRYPT_MODE,keySpce);
                //加密
                byte[] bytes = cipher.doFinal(content.getBytes(charset));
                return Base64.encodeToString(bytes,Base64.DEFAULT);
            }else if(type == DECODE){
                cipher.init(Cipher.DECRYPT_MODE,keySpce);
                byte[] bytes = cipher.doFinal(Base64.decode(content, Base64.DEFAULT));
                return new String(bytes,0,bytes.length);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }


        return  null;
    }

    public static String aes(String content,String key,int type){
        String charset = "UTF-8";
        try {
            byte[] keyBytes = key.getBytes(charset);
            byte[] temp = new byte[32];
            System.arraycopy(keyBytes,0,temp,0,Math.min(temp.length,keyBytes.length));
            //获取密钥对象
            Key keySpce = new SecretKeySpec(temp,"aes");
            //获取密码生成器
            Cipher cipher = Cipher.getInstance("aes");
            //加密
            if(type == ENCODE){
                //初始化密码生成器
                cipher.init(Cipher.ENCRYPT_MODE,keySpce);
                //加密
                byte[] bytes = cipher.doFinal(content.getBytes(charset));
                return Base64.encodeToString(bytes,Base64.DEFAULT);
            }else if(type == DECODE){
                cipher.init(Cipher.DECRYPT_MODE,keySpce);
                byte[] bytes = cipher.doFinal(Base64.decode(content, Base64.DEFAULT));
                return new String(bytes,0,bytes.length);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }


        return  null;
    }

    public static String rsaEncode(String content){
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("rsa");
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(new BigInteger(MODULUS),new BigInteger(PUB_KEY));
            PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
            //获取密码生成器
            Cipher cipher = Cipher.getInstance("rsa");
            //初始化密码生成器
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //加密
            byte[] bytes = cipher.doFinal(content.getBytes("utf-8"));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return  null;
    }

    public static String rsaDecode(String content) {
        try {
            KeyFactory factory = KeyFactory.getInstance("rsa");
            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(new BigInteger(MODULUS), new BigInteger(PRI_KEY));
            PrivateKey privateKey = factory.generatePrivate(rsaPrivateKeySpec);
            Cipher cipher = Cipher.getInstance("rsa");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytes = cipher.doFinal(Base64.decode(content, Base64.DEFAULT));
            return new String(bytes, 0, bytes.length);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


}
