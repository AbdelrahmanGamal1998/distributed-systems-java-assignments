package messaging;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;

/** The REST API: post, read and delete messages, list topics and users, and manage topic subscriptions. */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {

    private final MessageStore store;

    public MessageResource(MessageStore store) {
        this.store = store;
    }

    private static Response error(Response.Status status, String message) {
        return Response.status(status).entity(new ApiError(message)).build();
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }

    @POST
    @Path("messages")
    public Response post(NewMessage request) {
        if (request == null || blank(request.sender()) || blank(request.topic()) || blank(request.content())) {
            return error(Response.Status.BAD_REQUEST, "sender, topic and content are required");
        }
        Message message = store.post(request.sender(), request.topic(), request.content());
        return Response.created(URI.create("/messages/" + message.id())).entity(message).build();
    }

    @GET
    @Path("messages")
    public Response list(@QueryParam("topic") String topic) {
        return Response.ok(store.messages(topic)).build();
    }

    @GET
    @Path("messages/{id}")
    public Response get(@PathParam("id") int id) {
        return store.find(id).map(message -> Response.ok(message).build())
                .orElseGet(() -> error(Response.Status.NOT_FOUND, "message " + id + " does not exist"));
    }

    @DELETE
    @Path("messages/{id}")
    public Response delete(@PathParam("id") int id) {
        return store.delete(id) ? Response.noContent().build()
                : error(Response.Status.NOT_FOUND, "message " + id + " does not exist");
    }

    @GET
    @Path("topics")
    public Response topics() {
        return Response.ok(store.topics()).build();
    }

    @GET
    @Path("users")
    public Response users() {
        return Response.ok(store.users()).build();
    }

    @PUT
    @Path("topics/{topic}/subscribers/{user}")
    public Response subscribe(@PathParam("topic") String topic, @PathParam("user") String user) {
        return Response.ok(store.subscribe(topic, user)).build();
    }

    @DELETE
    @Path("topics/{topic}/subscribers/{user}")
    public Response unsubscribe(@PathParam("topic") String topic, @PathParam("user") String user) {
        return store.unsubscribe(topic, user).map(list -> Response.ok(list).build())
                .orElseGet(() -> error(Response.Status.NOT_FOUND, "topic " + topic + " does not exist"));
    }

    @GET
    @Path("topics/{topic}/subscribers")
    public Response subscribers(@PathParam("topic") String topic) {
        return store.subscribers(topic).map(list -> Response.ok(list).build())
                .orElseGet(() -> error(Response.Status.NOT_FOUND, "topic " + topic + " does not exist"));
    }
}