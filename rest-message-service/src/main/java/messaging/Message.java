package messaging;

/** A stored message. The sender and topic also get numeric ids, as the assignment asks. */
public record Message(int id, int senderId, String sender, int topicId, String topic, String content,
        String timestamp) {
}