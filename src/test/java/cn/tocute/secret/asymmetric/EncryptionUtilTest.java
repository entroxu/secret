package cn.tocute.secret.asymmetric;

import cn.tocute.encryption.asymmetric.EncryptionUtil;
import cn.tocute.secret.asymmetric.helper.KeyPairGeneratorAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

class EncryptionUtilTest {

    public static final String RSA_ALGORITHM="RSA";

    //签名串有长度限制
    //public static final String SIGN_ALGORITHM="NONEwithRSA";
    public static final String SIGN_ALGORITHM="MD5withRSA";
    //RSA/ECB/OAEPWithMD5AndMGF1Padding 模式  最长原始长度94
    public static final String TRANSFORMATION="RSA/ECB/OAEPWithMD5AndMGF1Padding";

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


        KeyPair keyPair = EncryptionUtil.generator(KeyPairGeneratorAlgorithm.RSA);
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

            byte[] sign = EncryptionUtil.sign(SIGN_ALGORITHM,privateKey, messageByte);
            boolean signSuccess = EncryptionUtil.signVerify(SIGN_ALGORITHM,publicKey, messageByte, sign);
            System.out.println("\r2.验证签名:" + signSuccess);
            System.out.println("\r3.签名:" + Base64.getEncoder().encodeToString(sign));


            byte[] secret = EncryptionUtil.encrypt(RSA_ALGORITHM,publicKey, messageByte);
            byte[] message = EncryptionUtil.decrypt(RSA_ALGORITHM,privateKey, secret);
            System.out.println("\r4.密文：" + new String(Base64.getEncoder().encode(secret), StandardCharsets.UTF_8));
            System.out.println("\r5.解密文：" + new String(message, StandardCharsets.UTF_8));


            System.out.println("\r\r\n");
        }
    }
}