package xyz.fragbots.Utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    public static String hash(String uuid)
    {
        try {

            MessageDigest md = MessageDigest.getInstance("sha1");
            String salt = new BigInteger(130,new java.util.Random()).toString(32);
            String string = uuid + salt;
            return new java.math.BigInteger(md.digest(string.getBytes())).toString(16);
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
