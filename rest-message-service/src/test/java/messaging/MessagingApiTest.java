package messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/** Starts the real server on a free port and calls it over HTTP. */
@Timeout(20)
class MessagingApiTest {

    // Grizzly does not support the HTTP/2 upgrade that Java's client attempts by default for requests with a body.
    private final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    private final ObjectMapper json = new ObjectMapper();
    private HttpServer server;
    private String base;

    private record Reply(int status, String body) {
        JsonNode json() throws IOException {
            return new ObjectMapper().readTree(body);
        }
    }

    @BeforeEach
    void startServer() throws IOException {
        int port;
        try (ServerSocket free = new ServerSocket(0)) {
            port = free.getLocalPort();
        }
        base = "http://localhost:" + port;
        server = MessagingServer.start(URI.create(base + "/"), new MessageStore());
    }

    @AfterEach
    void stopServer() {
        server.shutdownNow();
    }

    private Reply call(String method, String path, String body) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(base + path)).header("Content-Type",
                "application/json");
        request.method(method, body == null ? BodyPublishers.noBody() : BodyPublishers.ofString(body));
        HttpResponse<String> response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
        return new Reply(response.statusCode(), response.body());
    }

    private Reply post(String sender, String topic, String content) throws Exception {
        return call("POST", "/messages", json.writeValueAsString(new NewMessage(sender, topic, content)));
    }

    private static List<String> texts(JsonNode array) {
        List<String> result = new ArrayList<>();
        array.forEach(node -> result.add(node.asText()));
        return result;
    }

    @Test
    void postingAMessageStoresItWithIdsAndATimestamp() throws Exception {
        Reply reply = post("alice", "sports", "Hello everyone");

        assertEquals(201, reply.status());
        JsonNode message = reply.json();
        assertEquals(1, message.get("id").asInt());
        assertEquals(1, message.get("senderId").asInt());
        assertEquals(1, message.get("topicId").asInt());
        assertEquals("Hello everyone", message.get("content").asText());
        assertTrue(message.get("timestamp").asText().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    void aMessageCanBeRetrievedAndDeletedById() throws Exception {
        post("alice", "sports", "first");

        assertEquals("first", call("GET", "/messages/1", null).json().get("content").asText());
        assertEquals(204, call("DELETE", "/messages/1", null).status());
        assertEquals(404, call("GET", "/messages/1", null).status());
        assertEquals(404, call("DELETE", "/messages/1", null).status());
    }

    @Test
    void messagesCanBeListedByTopic() throws Exception {
        post("alice", "sports", "goal");
        post("bob", "music", "new album");
        post("carol", "sports", "match tonight");

        assertEquals(3, call("GET", "/messages", null).json().size());
        JsonNode sports = call("GET", "/messages?topic=sports", null).json();
        assertEquals(2, sports.size());
        assertEquals("goal", sports.get(0).get("content").asText());
        assertEquals("match tonight", sports.get(1).get("content").asText());
    }

    @Test
    void topicsAndUsersAreListedOnceEach() throws Exception {
        post("alice", "sports", "a");
        post("alice", "music", "b");
        post("bob", "sports", "c");

        assertEquals(List.of("sports", "music"), texts(call("GET", "/topics", null).json()));
        JsonNode users = call("GET", "/users", null).json();
        assertEquals("alice", users.get("1").asText());
        assertEquals("bob", users.get("2").asText());
        assertEquals(2, users.size());
    }

    @Test
    void usersCanSubscribeAndUnsubscribe() throws Exception {
        post("alice", "sports", "hello");

        assertEquals(List.of("bob"), texts(call("PUT", "/topics/sports/subscribers/bob", null).json()));
        assertEquals(List.of("bob", "carol"), texts(call("PUT", "/topics/sports/subscribers/carol", null).json()));
        assertEquals(List.of("bob", "carol"), texts(call("GET", "/topics/sports/subscribers", null).json()));
        assertEquals(List.of("carol"), texts(call("DELETE", "/topics/sports/subscribers/bob", null).json()));
    }

    @Test
    void subscribingCreatesATopicThatDidNotExist() throws Exception {
        call("PUT", "/topics/news/subscribers/alice", null);

        assertEquals(List.of("news"), texts(call("GET", "/topics", null).json()));
    }

    @Test
    void anUnknownTopicIsReportedAsNotFound() throws Exception {
        assertEquals(404, call("GET", "/topics/nothing/subscribers", null).status());
        assertEquals(404, call("DELETE", "/topics/nothing/subscribers/bob", null).status());
    }

    @Test
    void anIncompleteMessageIsRejected() throws Exception {
        Reply reply = call("POST", "/messages", "{\"sender\":\"alice\",\"topic\":\"sports\"}");

        assertEquals(400, reply.status());
        assertTrue(reply.json().get("error").asText().contains("required"));
    }
}