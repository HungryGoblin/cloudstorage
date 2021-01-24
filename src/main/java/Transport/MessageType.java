package Transport;

public enum MessageType {
    FILE("FileMessage"),
    FILE_LIST("FileList");

    String className;

    MessageType(String className) {
        this.className = className;
    }

    public String toString() {
        return className;
    }
}
