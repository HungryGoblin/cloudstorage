package filesystem;

import transport.Envelope;
import transport.Message;
import transport.MessageType;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileList implements Serializable {

    Path root;

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
        setRoot(Paths.get(directoryPath));
    }

    public String getContent() {
        StringBuilder list = new StringBuilder();
        fileList.forEach(file -> {
            list.append("," + file);
        });
        return (list.deleteCharAt(0).toString());
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

        // проверка существования файла
        int size = fs.getFileList().size();
        System.out.println(new Envelope("test_user", new Message(fs, MessageType.FILE_LIST)));
        for (int i = 0; i < fs.getFileList().size(); i++) {
            Path path = Paths.get("sync\\server").resolve(fs.getPath(i));
            if (FileHelper.fileExists(path))
                System.out.printf("Path: %s hash: %s%n", path, new FileEntity(path).getHash());
            if(FileHelper.fileEntityEquals(fs.get(i), path)) fs.remove(i);
        }
        System.out.println(new Envelope("test_user", new Message(fs, MessageType.FILE_LIST)));
    }
}
