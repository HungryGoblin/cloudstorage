package Transport;

public class Message {

    MessageType type;
    Object message;

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return message.toString();
    }

    public Message(Object message, MessageType messageType) {
        this.message = message;
        this.type = messageType;
    }

}
