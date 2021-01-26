package transport;

public enum MessageType {
    FILE("FileMessage"),
    FILE_LIST("FileList"),
    COMMAND("Command");

    String className;

    MessageType(String className) {
        this.className = className;
    }

    public String toString() {
        return className;
    }
}
