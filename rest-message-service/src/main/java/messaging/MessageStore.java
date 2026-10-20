package messaging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

/** In-memory storage for messages, users, topics and topic subscriptions. Safe to use from many threads. */
public class MessageStore {

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Map<Integer, Message> messages = new LinkedHashMap<>();
    private final Map<String, Integer> userIds = new LinkedHashMap<>();
    private final Map<String, Integer> topicIds = new LinkedHashMap<>();
    private final Map<String, Set<String>> subscribers = new HashMap<>();
    private int nextMessageId = 1;

    public synchronized Message post(String sender, String topic, String content) {
        int senderId = userIds.computeIfAbsent(sender, name -> userIds.size() + 1);
        int topicId = topicIds.computeIfAbsent(topic, name -> topicIds.size() + 1);
        Message message = new Message(nextMessageId++, senderId, sender, topicId, topic, content,
                LocalDateTime.now().format(TIMESTAMP));
        messages.put(message.id(), message);
        return message;
    }

    public synchronized Optional<Message> find(int id) {
        return Optional.ofNullable(messages.get(id));
    }

    public synchronized boolean delete(int id) {
        return messages.remove(id) != null;
    }

    /** The messages of a topic, or all messages when the topic is null or blank. */
    public synchronized List<Message> messages(String topic) {
        List<Message> result = new ArrayList<>();
        for (Message message : messages.values()) {
            if (topic == null || topic.isBlank() || message.topic().equals(topic)) {
                result.add(message);
            }
        }
        return result;
    }

    public synchronized List<String> topics() {
        return new ArrayList<>(topicIds.keySet());
    }

    /** User id to user name. */
    public synchronized Map<Integer, String> users() {
        Map<Integer, String> result = new TreeMap<>();
        userIds.forEach((name, id) -> result.put(id, name));
        return result;
    }

    /** Subscribes a user to a topic (creating the topic if needed) and returns the topic's subscribers. */
    public synchronized List<String> subscribe(String topic, String user) {
        topicIds.computeIfAbsent(topic, name -> topicIds.size() + 1);
        userIds.computeIfAbsent(user, name -> userIds.size() + 1);
        subscribers.computeIfAbsent(topic, name -> new LinkedHashSet<>()).add(user);
        return new ArrayList<>(subscribers.get(topic));
    }

    /** Removes a subscription. Empty when the topic does not exist. */
    public synchronized Optional<List<String>> unsubscribe(String topic, String user) {
        if (!topicIds.containsKey(topic)) {
            return Optional.empty();
        }
        Set<String> current = subscribers.getOrDefault(topic, new LinkedHashSet<>());
        current.remove(user);
        return Optional.of(new ArrayList<>(current));
    }

    /** The subscribers of a topic. Empty when the topic does not exist. */
    public synchronized Optional<List<String>> subscribers(String topic) {
        if (!topicIds.containsKey(topic)) {
            return Optional.empty();
        }
        return Optional.of(new ArrayList<>(subscribers.getOrDefault(topic, new LinkedHashSet<>())));
    }
}