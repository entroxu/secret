package cn.tocute;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        String s = "+";
        String b64encoded = Base64.getUrlEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
        System.out.println(b64encoded);
        byte[] output = Base64.getUrlDecoder().decode(b64encoded);
        System.out.println(Arrays.toString(output));

        new Object();

    }
}