package filesystem;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileEntity implements Serializable {

    private transient Path path;

    private String stringPath;

    private String fileName;

    private String hash;

    public String getFileName() {
        return fileName;
    }

    public Path getPath() {
        if (path == null) path = Paths.get(getStringPath());
        return path;
    }

    public String getStringPath() {
        return stringPath;
    }

    public String getHash() {
        return hash;
    }

    public void setStringPath(String stringPath) {
        this.stringPath = stringPath;
    }

    @Override
    public boolean equals(Object obj) {
        FileEntity trgEntity = (FileEntity)obj;
        return this.getHash() == trgEntity.getHash();
    }

    @Override
    public String toString() {
        return (String.format("%s:%s", path == null? stringPath: path.normalize(), hash));
    }

    public FileEntity(Path path) throws IOException {
        this(path, Paths.get(""));
    }

    public FileEntity(Path path, Path root) throws IOException {
        FileHelper.fileExists(path, true);
        this.path = root.relativize(path);
        this.hash = CryptoHelper.getChecksum(path);
        this.stringPath = this.path.toString();
        this.fileName = path.getFileName().toString();
    }
}
