package dev.cromo29.durkcore.Util;

import java.util.Base64;

public class Encrypt {

    public static String decode64(String string) {
        return new String(Base64.getDecoder().decode(string.getBytes()));
    }

    public static String encode64(String string) {
        return new String(Base64.getEncoder().encode(string.getBytes()));
    }
}
