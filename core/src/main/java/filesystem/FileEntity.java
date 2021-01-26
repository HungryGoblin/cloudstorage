package filesystem;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileEntity {

    private Path path;

    private String hash;

    public Path getPath() {
        return path;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        FileEntity trgEntity = (FileEntity)obj;
        return this.getHash() == trgEntity.getHash();
    }

    @Override
    public String toString() {
        return String.format("%s:%s", path.normalize(), hash);
    }

    public FileEntity(Path path) throws IOException {
        this(path, Paths.get(""));
    }

    public FileEntity(Path path, Path root) throws IOException {
        FileHelper.fileExists(path, true);
        this.path = root.relativize(path);
        this.hash = CryptoHelper.getChecksum(path);
    }
}
