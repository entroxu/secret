package cn.tocute.encryption.symmetric;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static cn.tocute.encryption.symmetric.EncryptionUtil.*;
import static org.junit.jupiter.api.Assertions.*;

@TestClassOrder(value = ClassOrderer.OrderAnnotation.class)
class EncryptionUtilTest {

    public static final String AES_ALGORITHM="AES";

    //public static final String TRANSFORMATION="AES/GCM/NoPadding";
    //AES/ECB/PKCS5Padding  ECB 支持128 不用偏移量iv,每次输出密文一样
    //AES/GCM/NoPadding 默认提供上点支持128 ，每次密文不一样
    @Test
    @Order(1)
    public void generatorKey() throws NoSuchAlgorithmException {
        String key = EncryptionUtil.generateKey(AES_ALGORITHM);
        System.out.println(key);
    }

    @Test
    @Order(2)
    public void getKey(){
        String keyString= "ghLbHgi6KacJWs4iEvF3tw==";
        SecretKey secretKey = EncryptionUtil.getKey(AES_ALGORITHM, keyString);
    }

    @Test
    @Order(3)
    public void ecbEncrypt() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String keyString= "ghLbHgi6KacJWs4iEvF3tw==";
        String message = "我今天没吃饭";
        SecretKey secretKey = EncryptionUtil.getKey(AES_ALGORITHM, keyString);
        byte[] encrypted = EncryptionUtil.ecbEncrypt(ECB_TRANSFORMATION, secretKey, message.getBytes(StandardCharsets.UTF_8));

        System.out.println("加密后：" + new String(Base64.getEncoder().encode(encrypted),StandardCharsets.UTF_8));
    }
    @Test
    @Order(4)
    public void ecbDecrypt() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String keyString= "ghLbHgi6KacJWs4iEvF3tw==";
        String secret = "3U9Ys/PYSkh/++LG7bALnvKoEWBQ3Qxc7K92C4BNj9k=";

        SecretKey secretKey = EncryptionUtil.getKey(AES_ALGORITHM, keyString);
        byte[] decrypted = EncryptionUtil.ecbDecrypt(ECB_TRANSFORMATION, secretKey, Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8)));
        System.out.println("解密：" + new String(decrypted,StandardCharsets.UTF_8));
    }



    @Test
    @Order(3)
    public void cbcEncrypt() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String keyString= "ghLbHgi6KacJWs4iEvF3tw==";
        String message = "我今天没吃饭";
        SecretKey secretKey = EncryptionUtil.getKey(AES_ALGORITHM, keyString);
        byte[] encrypted = EncryptionUtil.cbcEncrypt(CBC_TRANSFORMATION, secretKey, message.getBytes(StandardCharsets.UTF_8));

        System.out.println("加密后：" + new String(Base64.getEncoder().encode(encrypted),StandardCharsets.UTF_8));
    }
    @Test
    @Order(4)
    public void cbcDecrypt() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String keyString= "ghLbHgi6KacJWs4iEvF3tw==";
        String secret = "Fm6Ypg4FhJXVzmJ7vt7oeDTl2BPyS+boR+C8loQemmiaP5BoVdcxsnomO2VHoyiI";

        SecretKey secretKey = EncryptionUtil.getKey(AES_ALGORITHM, keyString);
        byte[] decrypted = EncryptionUtil.cbcDecrypt(CBC_TRANSFORMATION, secretKey, Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8)));
        System.out.println("解密：" + new String(decrypted,StandardCharsets.UTF_8));
    }


    @Test
    @Order(3)
    public void gcmEncrypt() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String keyString= "ghLbHgi6KacJWs4iEvF3tw==";
        String message = "我今天没吃饭";
        SecretKey secretKey = EncryptionUtil.getKey(AES_ALGORITHM, keyString);
        byte[] encrypted = EncryptionUtil.gcmEncrypt(GCM_TRANSFORMATION, secretKey, message.getBytes(StandardCharsets.UTF_8));

        System.out.println("加密后：" + new String(Base64.getEncoder().encode(encrypted),StandardCharsets.UTF_8));
    }
    @Test
    @Order(4)
    public void gcmDecrypt() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String keyString= "ghLbHgi6KacJWs4iEvF3tw==";
        String secret = "dKIb3SIe8nO7iL5ZI4vs2EWAXCzsny3nJwb4gD+wF4AfoeeP+SqS6m7hTbds7DBRDAQ=";

        SecretKey secretKey = EncryptionUtil.getKey(AES_ALGORITHM, keyString);
        byte[] decrypted = EncryptionUtil.gcmDecrypt(GCM_TRANSFORMATION, secretKey, Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8)));
        System.out.println("解密：" + new String(decrypted,StandardCharsets.UTF_8));
    }

    @Test
    @Order(5)
    public void test(){}

}