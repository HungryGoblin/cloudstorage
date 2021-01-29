package transport;

import com.sun.istack.internal.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Container implements Serializable {

    private String user;
    private LocalDateTime created;
    private MessageType messageType;
    private Object message;


    public String getUser() {
        return user;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Container(@NotNull String user, Object message, MessageType messageType) {
        this.user = user;
        this.created = LocalDateTime.now();
        this.message = message;
        this.messageType = messageType;
    }

    public Container(@NotNull String user, String message) {
        this(user, message, MessageType.MESSAGE);
    }

    @Override
    public String toString() {
        return "user='" + user + '\'' +
                ",type=" + getMessageType() + '\'' +
                ",created=" + created + '\'' +
                ",message=" + message.toString();
    }
}
