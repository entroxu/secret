package cn.tocute.secret.rsa;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class SecretUtilTest {

    @BeforeEach
    void setUp() {
        assert true;
    }

    @Test
    @Order(5)
    void generator() {
        System.out.println("order 5");
        assert true;
    }

    @Test
    @Order(1)
    void getPrivateKey() {
        System.out.println("order 1");
    }

    @Test
    void getPublicKey() {
    }

    @Test
    void getPrivateKeyByte() {
    }

    @Test
    void getPublicKeyByte() {
    }

    @Test
    void encrypt() {
    }

    @Test
    void decrypt() {
    }

    @Test
    void sign() {
    }

    @Test
    void signVerify() {
    }

    @Test
    void signOther() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {


        KeyPair keyPair = SecretUtil.generator();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        System.out.println("生成密钥：私钥：" + new String(Base64.getEncoder().encode(privateKey.getEncoded())));
        System.out.println("生成密钥：公钥：" + new String(Base64.getEncoder().encode(publicKey.getEncoded())));

        int[] ints = new int[]{94, 80, 60};

        for (int i = 0; i < 3; i++) {
            StringBuilder messageBuilder = new StringBuilder();
            for (int j = 0; j < ints[i]; j++) {
                messageBuilder.append('a');
            }
            String tem = messageBuilder.toString();

            byte[] messageByte = tem.getBytes(StandardCharsets.UTF_8);
            System.out.println("1.长度：" + tem.length() + " 原文：" + tem);

            byte[] sign = SecretUtil.sign(privateKey, messageByte);
            boolean signSuccess = SecretUtil.signVerify(publicKey, messageByte, sign);
            System.out.println("\r2.验证签名:" + signSuccess);
            System.out.println("\r3.签名:" + Base64.getEncoder().encodeToString(sign));


            byte[] secret = SecretUtil.encrypt(publicKey, messageByte);
            byte[] message = SecretUtil.decrypt(privateKey, secret);
            System.out.println("\r4.密文：" + new String(Base64.getEncoder().encode(secret), StandardCharsets.UTF_8));
            System.out.println("\r5.解密文：" + new String(message, StandardCharsets.UTF_8));


            System.out.println("\r\r\n");
        }
    }
}