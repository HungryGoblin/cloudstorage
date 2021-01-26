package transport;

import com.sun.istack.internal.NotNull;
import java.time.LocalDateTime;

public class Envelope {

    String user;
    Message message;
    LocalDateTime created;

    public Envelope(@NotNull String user, @NotNull Message message) {
        this.user = user;
        this.message = message;
        this.created = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "FileMessage{" +
                "user='" + user + '\'' +
                ",type=" + message.getType() + '\'' +
                ",created=" + created + '\'' +
                ",type=" + message.toString() + '\'' +
                ",message=" + message.toString() +
                '}';
    }
}
