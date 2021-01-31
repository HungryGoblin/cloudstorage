package tools;

import filesystem.FileHelper;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoHelper {

    private static final String HASH = "MD5";

    private static MessageDigest md;

    public static String getChecksum (byte[] byteArray) {
        md.update(byteArray);
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    public static String getChecksum (Path file) throws IOException {
        FileHelper.fileExists(file, true);
        return getChecksum(Files.readAllBytes(file));
    }

    public static String getChecksum (String string) {
        return getChecksum(string.getBytes());
    }

    static {
        try {
            md = MessageDigest.getInstance(HASH);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
