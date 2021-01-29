package filesystem;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileList implements Serializable, Cloneable {

    private transient Path root;

    public ArrayList<FileEntity> fileList = new ArrayList<>();

    public Path getRoot() {
        return root.normalize();
    }

    public ArrayList<FileEntity> getFileList() {
        return fileList;
    }

    public void setRoot(Path root) throws IOException {
        FileHelper.directoryExists(root, true);
        this.root = root.normalize();
        List<Path> files = Files.walk(root)
                .filter(file -> !Files.isDirectory(file))
                .collect(Collectors.toList());
        for (Path file : files) {
            fileList.add(new FileEntity(file, root));
        }
    }

    public FileEntity get(int i) {
        return fileList.get(i);
    }

    public void remove(int i) {
        if (i < 0 || i > size()) throw
                new ArrayIndexOutOfBoundsException(String.format("Illegal index %d, expected 0-%d", i, size()));
        fileList.remove(i);
    }

    public int size() {
        return fileList.size();
    }

    public Path getPath(int i) {
        return fileList.get(i).getPath();
    }

    public FileList(String directoryPath) throws IOException {
        this(Paths.get(directoryPath));
    }

    public FileList(Path directoryPath) throws IOException {
        setRoot(directoryPath);
    }

    public String getContent() {
        StringBuilder list = new StringBuilder();
        fileList.forEach(file -> {
            list.append("," + file);
        });
        if (list.length() > 0) list.deleteCharAt(0).toString();
        return list.toString();
    }

    @Override
    public String toString() {
        return getContent();
    }

    public static void main(String[] args) throws IOException {
        FileList fs = new FileList("sync\\client");
        fs.getFileList().forEach(System.out::println);

        // копирование
        for (int i = 0; i < fs.getFileList().size(); i++) {
            FileHelper.createDirectoryIfNotExists(fs.getPath(i), Paths.get("sync\\server"));
        }

    }
}
