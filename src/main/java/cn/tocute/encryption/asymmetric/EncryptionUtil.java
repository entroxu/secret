package cn.tocute.encryption.asymmetric;

import cn.tocute.secret.asymmetric.helper.KeyPairGeneratorAlgorithm;

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
 * 密钥和私钥要存为string必须使用base64编码byte[],不能直接转为string因为byte[]中可能有非String类字符
 */
public class EncryptionUtil {

    public static KeyPair generator(KeyPairGeneratorAlgorithm algorithms) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithms.getName());
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public static PrivateKey getPrivateKey(KeyPairGeneratorAlgorithm algorithm, byte[] privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getName());
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }
    public static PublicKey getPublicKey(KeyPairGeneratorAlgorithm algorithm,byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getName());
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }
    public static byte[] getPrivateKeyByte(KeyPair keyPair){
        return keyPair.getPrivate().getEncoded();
    }
    public static byte[] getPublicKeyByte(KeyPair keyPair){
        return keyPair.getPublic().getEncoded();
    }


    public static byte[] encrypt(String transformation,PublicKey publicKey,byte[] message) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        return cipher.doFinal(message);
    }
    public static byte[] decrypt(String transformation,PrivateKey privateKey,byte[] secret) throws NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        return cipher.doFinal(secret);
    }

    public static byte[] sign(String algorithm,PrivateKey privateKey,byte[] message) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(message);
        return signature.sign();
    }
    public static boolean signVerify(String algorithm,PublicKey publicKey,byte[] message,byte[] digitalSignature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(message);
        return signature.verify(digitalSignature);
    }

}
