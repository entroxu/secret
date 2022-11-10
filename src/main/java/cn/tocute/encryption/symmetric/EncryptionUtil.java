package cn.tocute.encryption.symmetric;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 公钥加密，私钥解密
 */
public class EncryptionUtil {

    public static final String GCM_TRANSFORMATION="AES/GCM/NoPadding";
    public static final String ECB_TRANSFORMATION="AES/ECB/PKCS5Padding";

    public static final String CBC_TRANSFORMATION="AES/CBC/PKCS5Padding";

    public static String generateKey(String algorithm) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        byte[] keyEncoded = key.getEncoded();
        return new String(Base64.getEncoder().encode(keyEncoded), StandardCharsets.UTF_8);
    }
    public static SecretKey getKey(String algorithm,String keyString){
        byte[] keyByte = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8),algorithm);
    }


    public static byte[] ecbEncrypt(String transformation, SecretKey key, byte[] message) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        return cipher.doFinal(message);
    }
    public static byte[] ecbDecrypt(String transformation, SecretKey key, byte[] encrypted) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE,key);
        return cipher.doFinal(encrypted);
    }
    public static byte[] cbcEncrypt(String transformation, SecretKey key, byte[] message) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        // CBC模式需要生成一个16 bytes的initialization vector:
        SecureRandom sr = SecureRandom.getInstanceStrong();
        byte[] iv = sr.generateSeed(16);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE,key,ivParameterSpec);
        byte[] data = cipher.doFinal(message);
        // IV不需要保密，把IV和密文一起返回:
        return join(iv, data);
    }
    public static byte[] cbcDecrypt(String transformation, SecretKey key, byte[] encrypted) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] iv = new byte[16];
        byte[] data = new byte[encrypted.length - 16];
        System.arraycopy(encrypted, 0, iv, 0, 16);
        System.arraycopy(encrypted, 16, data, 0, data.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE,key,ivParameterSpec);
        return cipher.doFinal(data);
    }

    public static byte[] gcmEncrypt(String transformation, SecretKey key, byte[] message) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        // CBC模式需要生成一个16 bytes的initialization vector:
        SecureRandom sr = SecureRandom.getInstanceStrong();
        byte[] iv = sr.generateSeed(16);
        GCMParameterSpec ivParameterSpec = new GCMParameterSpec(128,iv);

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE,key,ivParameterSpec);
        byte[] data = cipher.doFinal(message);
        // IV不需要保密，把IV和密文一起返回:
        return join(iv, data);
    }
    public static byte[] gcmDecrypt(String transformation, SecretKey key, byte[] encrypted) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] iv = new byte[16];
        byte[] data = new byte[encrypted.length - 16];
        System.arraycopy(encrypted, 0, iv, 0, 16);
        System.arraycopy(encrypted, 16, data, 0, data.length);
        GCMParameterSpec ivParameterSpec = new GCMParameterSpec(128,iv);

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE,key,ivParameterSpec);
        return cipher.doFinal(data);
    }

    public static byte[] join(byte[] bs1, byte[] bs2) {
        byte[] r = new byte[bs1.length + bs2.length];
        System.arraycopy(bs1, 0, r, 0, bs1.length);
        System.arraycopy(bs2, 0, r, bs1.length, bs2.length);
        return r;
    }
}
