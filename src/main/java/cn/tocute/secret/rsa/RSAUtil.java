package cn.tocute.secret.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 公钥加密，私钥解密
 * 公钥byte[]编码是：X509EncodedKeySpec
 * 私钥byte[]编码是：PKCS8EncodedKeySpec
 * 密钥和私钥要存为string必须使用base64编码byte[],不能直接转为string因为byte[]中可能有字符
 */
public class RSAUtil {
    public static final String RSA_ALGORITHM="RSA";

    public static KeyPair generator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public static PrivateKey getPrivateKey(byte[] privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }
    public static PublicKey getPublicKey(byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }
    public static byte[] getPrivateKeyByte(KeyPair keyPair){
        return keyPair.getPrivate().getEncoded();
    }
    public static byte[] getPublicKeyByte(KeyPair keyPair){
        return keyPair.getPublic().getEncoded();
    }


    public static byte[] encrypt(PublicKey publicKey,byte[] message) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        return cipher.doFinal(message);
    }
    public static byte[] decrypt(PrivateKey privateKey,byte[] secret) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        return cipher.doFinal(secret);
    }

}