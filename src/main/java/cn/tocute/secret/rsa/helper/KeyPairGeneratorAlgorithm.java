package cn.tocute.secret.rsa.helper;

public enum KeyPairGeneratorAlgorithm {
    DiffieHellman("DiffieHellman"),
    DSA("DSA"),
    RSA("RSA"),
    RSASSA_PSS("RSASSA-PSS"),
    EC("EC"),
    XDH("XDH"),
    X25519("X25519"),
    X448("X448"),


    ;


    String name;

    KeyPairGeneratorAlgorithm(java.lang.String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
