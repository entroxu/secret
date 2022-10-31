package cn.tocute.secret.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, NoSuchProviderException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        String privateKeyString = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIUMqICOiMQl69MJdpS2wGRKJrOLBee3v/MnyoqHxMX7ipqbd6MDBDGlYZ3P5JatHUBdntlWf5cXoKHP2cIUosl2iNnk+d2l2hrdQC0oOESGBl1yL/uFQsB++giKnsyztITT/wj30eSI6BE0LqV5/1MbV5a6mY67gxr08RKPr9nlAgMBAAECgYB17EA0oSAq6YlYpJyLNmbS/L3MiD2VoNDqTKxBmBAZgdbZLU4eFwBSmEgqvSXXPlwcvl2aOAYh/nkKsdRJJk0YhDzJZcXfre68tnI4WEIYZ+f8TalLk5uId9EoMlRCKX+rmBZu/BP+aCwNN9aL4r5xFjxTsYsAisNQOk7M4HRiyQJBANQPGt/5NzBflCCOr/ka4oZcNuf8iEz1N2wNdjX2lXpi60ifzdm/2ARjJtv4aRcXWH9XVtjQ6QDHsqI1C3a5+k8CQQCgnmhIQG86XdvRglPuFE9LVeaFhbFgPI7wPZp4AFUP5mtUuw5LG74jY9iVdMauMIOsEEiQmVkq5b1AsgkgzL+LAkEA0yFe3pQgMJdYwbmO1vO/iYqSWBMEGasjzP7yLPQfC0UlgJ8qspvhFS0q6WPALe9eexqYKzKEafRplDo/+atc3QJBAJVcuXz27nhIb/IUUBhFqrdT2lqwSJJFvmaJ4utU48U1cPoKOPB/jAE9CFtzjI7PbqLDQbe14sWRfrx60yI9RKECQC+rpVL+MovCJ+q/1PJbsL4L3OZD14/jMHqc/DWmn/fbHs+RQwXmd//6bryttEcBo6+O19fo45YipwukEWf6AkQ=";

        String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFDKiAjojEJevTCXaUtsBkSiaziwXnt7/zJ8qKh8TF+4qam3ejAwQxpWGdz+SWrR1AXZ7ZVn+XF6Chz9nCFKLJdojZ5Pndpdoa3UAtKDhEhgZdci/7hULAfvoIip7Ms7SE0/8I99HkiOgRNC6lef9TG1eWupmOu4Ma9PESj6/Z5QIDAQAB";


        String json = "{\"lcid\":\"2052\",\"entryrole\":\"XT\",\"jsonInfo\":\"{\\\"eid\\\":\\\"2742875\\\",\\\"signature\\\":\\\"E8FwdwIb09ZB5mM3tgK6tS266QrXLqK+xI0YtjJwQcg5VS9+kGcRTP0QqEmCmn54CfK6fHReY+g8JvIATMpflf0jvml6k2J56fZVJvCYgM3keB44UeKwIr5v5ftrGsL8JFoLe2xmpvU/HbgrFyrsZuHMTTVTMsV0qYEmeqj4n+I=\\\",\\\"openid\\\":\\\"6357493ae4b0c9a13fc14f4e\\\",\\\"appid\\\":\\\"yzj_ai_robot_test_2742840\\\",\\\"nonce\\\":\\\"MV68xo51uwFf1Zv8\\\",\\\"timestamp\\\":\\\"1666841431907\\\"}\",\"acctID\":\"5eccbe1301c99a\"}";



        int[] ints = new int[]{94, 95,118};

        for (int i = 0; i < 3; i++) {
            StringBuilder messageBuilder = new StringBuilder();
            for (int j = 0; j < ints[i]; j++) {
                messageBuilder.append('a');
            }
            String tem = messageBuilder.toString();

            byte[] messageByte = tem.getBytes(StandardCharsets.UTF_8);
            System.out.println("1.长度：" + tem.length() +" 原文：" + tem);
            PrivateKey privateKey = RSAUtil.getPrivateKey(Base64.getDecoder().decode(privateKeyString.getBytes(StandardCharsets.UTF_8)));
            PublicKey publicKey = RSAUtil.getPublicKey(Base64.getDecoder().decode(publicKeyString.getBytes(StandardCharsets.UTF_8)));


                byte[] sign = RSAUtil.sign(privateKey, messageByte);
                boolean signSuccess = RSAUtil.signVerify(publicKey, messageByte, sign);
                System.out.println("\r2.验证签名:" + signSuccess);
                System.out.println("\r3.签名:" + Base64.getEncoder().encodeToString(sign));


                byte[] secret = RSAUtil.encrypt(publicKey, messageByte);
                byte[] message = RSAUtil.decrypt(privateKey, secret);
                System.out.println("\r4.密文：" + new String(Base64.getEncoder().encode(secret), StandardCharsets.UTF_8));
                System.out.println("\r5.解密文：" + new String(message, StandardCharsets.UTF_8));


            System.out.println("\r\r\n");

        }


    }
}
