package messaging;

/** What a client sends to post a message. */
public record NewMessage(String sender, String topic, String content) {
}