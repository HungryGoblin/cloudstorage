package transport;

import filesystem.FileEntity;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class FileMessage implements Serializable {

    public static final int MAX_SIZE = 32000;

    private final byte[] data;

    private FileEntity fileEntity;
    private int partId = 0;
    private int partsTotal = 1;

    public int getPartId() {
        return partId;
    }

    public int getPartsTotal() {
        return partsTotal;
    }

    public String getFileName() {
        return fileEntity.getFileName();
    }

    private int calcPartsTotal() {
        return (int) Math.ceil(data.length / MAX_SIZE);
    }

    public Path getPath() {
        return fileEntity.getPath();
    }

    public void setStringPath(String path) {
        fileEntity.setStringPath(path);
    }

    public String getHash() {
        return fileEntity.getHash();
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage getPart(int partId) throws IOException {
        if (partId > partsTotal) throw new IOException(
                String.format("Invalid file part index: %d expected 0-%d", partId, partsTotal - 1));
        return new FileMessage(
                fileEntity,
                Arrays.copyOfRange(data, partId * MAX_SIZE, (partId + 1) * MAX_SIZE),
                partId,
                partsTotal);
    }

    public boolean saveFile(Path filePath) throws IOException {
        if (partId == 0)
            Files.write(filePath, getData(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        else
            Files.write(filePath, getData(), StandardOpenOption.APPEND);
        return true;
    }

    @Override
    public String toString() {
        return "FileMessage{" + "name=\'" + getFileName() + "\'" + (
                (getPartsTotal() > 1)? String.format(" (%d/%d)", getPartId() + 1, getPartsTotal()):"") + "}";
    }

    public FileMessage(Path path) throws IOException {
        if (!Files.exists(path)) throw new IOException(String.format("File does not exist: %s", path.toAbsolutePath()));
        this.fileEntity = new FileEntity(path);
        this.data = Files.readAllBytes(path);
        this.partsTotal = calcPartsTotal();
    }

    public FileMessage(FileEntity fileEntity, byte[] data, int partId, int parts) throws IOException {
        this.fileEntity = fileEntity;
        this.data = data;
        this.partId = partId;
        this.partsTotal = parts;
    }

}



