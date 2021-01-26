package filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {

    public static boolean directoryExists(Path path) {
        return Files.exists(path) && Files.isDirectory(path);
    }

    public static boolean directoryExists(Path path, boolean genError) throws IOException {
        boolean exists = Files.exists(path) && Files.isDirectory(path);
        if (!exists && genError)
            throw new IOException(String.format("Directory '%s' does not exist", path.toAbsolutePath()));
        return exists;
    }

    public static boolean fileExists(Path path) throws IOException {
        return fileExists(path, false);
    }

    public static boolean fileExists(Path path, boolean genError) throws IOException {
        boolean exists = Files.exists(path) && !Files.isDirectory(path);
        if (!exists && genError)
            throw new IOException(String.format("File '%s' does not exist", path.toAbsolutePath()));
        return exists;
    }

    public static boolean createDirectoryIfNotExists(Path path) throws IOException {
        return createDirectoryIfNotExists(path, null);
    }

    public static boolean createDirectoryIfNotExists(Path path, Path root) throws IOException {
        if (root != null) path = root.resolve(path);
        for (int i = 1; i < path.getNameCount(); i++) {
            Path dir = path.subpath(0, i);
            if (!directoryExists(dir)) Files.createDirectory(dir);
        }
        return directoryExists(path);
    }

    // сравнение объекта FileEntity с реальным файлом
    public static boolean fileEntityEquals(FileEntity srcFile, Path trgFile) throws IOException {
        return (fileEntityEquals(srcFile.getPath(), srcFile.getHash(), trgFile));
    }

    // сравнение имени файла, его контрольной суммы с реальным файлом
    public static boolean fileEntityEquals(Path srcFile, String srcHash, Path trgFile) throws IOException {
        if (!fileExists(trgFile)) return false;
        if (!srcFile.getFileName().equals(trgFile.getFileName())) return false;
        return (srcHash.equals(CryptoHelper.getChecksum(trgFile)));
    }

    // сравнение существующих файлов
    public static boolean fileEntityEquals(Path srcFile, Path trgFile) throws IOException {
        fileExists(srcFile);
        return fileEntityEquals(srcFile, CryptoHelper.getChecksum(srcFile), trgFile);
    }

}
