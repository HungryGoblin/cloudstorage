package FileSystem;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoHelper {

    private static final String HASH = "MD5";

    private static MessageDigest md;

    public static String getChecksum (Path file) throws IOException {
        FileHelper.fileExists(file, true);
        md.update(Files.readAllBytes(file));
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    static {
        try {
            md = MessageDigest.getInstance(HASH);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
