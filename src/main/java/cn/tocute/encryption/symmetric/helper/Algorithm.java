package cn.tocute.encryption.symmetric.helper;

public enum Algorithm {

    AES("AES"),

    ;
    String name;

    Algorithm(java.lang.String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
