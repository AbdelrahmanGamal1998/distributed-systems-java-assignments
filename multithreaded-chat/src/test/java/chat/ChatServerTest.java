package chat;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/** Starts the real chat server and talks to it over sockets, the way the GUI clients do. */
@Timeout(30)
class ChatServerTest {

    private static final int PORT = 18818;

    @BeforeAll
    static void startServer() throws Exception {
        Server server = new Server(PORT);
        server.setDaemon(true);
        server.start();

        long deadline = System.currentTimeMillis() + 5000;
        while (true) {
            try (Socket probe = new Socket("127.0.0.1", PORT)) {
                return;
            } catch (IOException notReadyYet) {
                if (System.currentTimeMillis() > deadline) {
                    throw notReadyYet;
                }
                Thread.sleep(100);
            }
        }
    }

    /** A connected chat client that remembers everything the server sent. */
    private static final class ChatClient implements AutoCloseable {
        private final Socket socket;
        private final InputStream in;
        private final OutputStream out;
        private final StringBuilder received = new StringBuilder();

        ChatClient() throws IOException {
            socket = new Socket("127.0.0.1", PORT);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        }

        void send(String line) throws IOException {
            out.write((line + "\n").getBytes(StandardCharsets.UTF_8));
            out.flush();
        }

        /** Waits up to three seconds for the given text to arrive. */
        boolean waitFor(String text) throws Exception {
            long deadline = System.currentTimeMillis() + 3000;
            while (System.currentTimeMillis() < deadline) {
                while (in.available() > 0) {
                    received.append((char) in.read());
                }
                if (received.toString().contains(text)) {
                    return true;
                }
                Thread.sleep(20);
            }
            return false;
        }

        boolean logIn(String user, String password) throws Exception {
            send("login " + user + " " + password);
            return waitFor("ok login");
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }
    }

    @Test
    void aNewUserCanRegisterAndLogIn() throws Exception {
        String user = "user" + System.nanoTime();
        try (ChatClient client = new ChatClient()) {
            client.send("Reg " + user + " secret1");
            assertTrue(client.waitFor("ok Registered"));

            assertTrue(client.logIn(user, "secret1"));
        }
    }

    @Test
    void aTakenUsernameIsRejected() throws Exception {
        try (ChatClient client = new ChatClient()) {
            client.send("Reg alice anything");

            assertTrue(client.waitFor("username already taken"));
        }
    }

    @Test
    void aWrongPasswordIsRejected() throws Exception {
        try (ChatClient client = new ChatClient()) {
            client.send("login alice wrong-password");

            assertTrue(client.waitFor("password Incorrect"));
        }
    }

    @Test
    void aDirectMessageReachesOnlyItsRecipient() throws Exception {
        try (ChatClient alice = new ChatClient(); ChatClient bob = new ChatClient();
                ChatClient waiting = new ChatClient()) { // connected but not logged in
            assertTrue(alice.logIn("alice", "alice123"));
            assertTrue(bob.logIn("bob", "bob123"));

            alice.send("msg bob see-you-at-noon");

            assertTrue(bob.waitFor("msg alice see-you-at-noon"));
        }
    }

    @Test
    void aMessageToAllReachesEveryoneLoggedIn() throws Exception {
        try (ChatClient alice = new ChatClient(); ChatClient bob = new ChatClient()) {
            assertTrue(alice.logIn("alice", "alice123"));
            assertTrue(bob.logIn("bob", "bob123"));

            alice.send("msg all hello-room");

            assertTrue(bob.waitFor("msg all:alice hello-room"));
        }
    }

    @Test
    void aMessageToATopicReachesOnlyThoseWhoJoined() throws Exception {
        try (ChatClient alice = new ChatClient(); ChatClient bob = new ChatClient()) {
            assertTrue(alice.logIn("alice", "alice123"));
            assertTrue(bob.logIn("bob", "bob123"));
            bob.send("join #java");
            Thread.sleep(200);

            alice.send("msg #java topic-news");

            assertTrue(bob.waitFor("msg #java:alice topic-news"));
        }
    }
}