package messaging;

import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/** Starts the REST service. Set the PORT environment variable to change the port (default 8080). */
public final class MessagingServer {

    private MessagingServer() {
    }

    public static HttpServer start(URI baseUri, MessageStore store) {
        ResourceConfig config = new ResourceConfig().register(new MessageResource(store))
                .register(JacksonFeature.class);
        return GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
    }

    public static void main(String[] args) throws IOException {
        String port = System.getenv().getOrDefault("PORT", "8080");
        HttpServer server = start(URI.create("http://0.0.0.0:" + port + "/"), new MessageStore());
        System.out.println("Message service running at http://localhost:" + port + "/  (press Enter to stop)");
        System.in.read();
        server.shutdownNow();
    }
}