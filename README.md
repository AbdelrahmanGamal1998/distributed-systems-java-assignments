# Distributed Systems Assignments (Java)

[![Build](https://github.com/AbdelrahmanGamal1998/distributed-systems-java-assignments/actions/workflows/build.yml/badge.svg)](https://github.com/AbdelrahmanGamal1998/distributed-systems-java-assignments/actions/workflows/build.yml)

Three assignments from my Software Engineering for Distributed Systems course (2021), in one Maven project:
a REST messaging service, a multithreaded chat application, and a peer-to-peer key/value system with Java RMI.
`mvn verify` builds all three and runs 21 tests.

| Module                                                  | Assignment                                                            | Tests |
| ------------------------------------------------------- | --------------------------------------------------------------------- | ----- |
| [`rest-message-service`](rest-message-service)          | Message management system as a REST web service (Jersey and Grizzly)  | 8, over real HTTP |
| [`multithreaded-chat`](multithreaded-chat)              | Multi-client chat with a Swing GUI, over sockets and threads          | 6, over real sockets |
| [`rmi-peer-to-peer`](rmi-peer-to-peer)                  | Peer-to-peer system that stores `<key, value>` pairs, with Java RMI   | 7, for the graph data structure |

You need JDK 17 or newer and Maven.

## REST message service

Users post messages to topics, read them back by id or by topic, and subscribe to topics.

| Method and path                         | What it does                                                         |
| --------------------------------------- | -------------------------------------------------------------------- |
| `POST /messages`                        | Store a message: `{"sender": "...", "topic": "...", "content": "..."}`. Returns 201 with the id, sender id, topic id and timestamp |
| `GET /messages?topic=sports`            | List the messages of a topic (all messages without `topic`)          |
| `GET /messages/{id}`                    | Retrieve one message (404 if it does not exist)                      |
| `DELETE /messages/{id}`                 | Delete a message                                                     |
| `GET /topics`, `GET /users`             | List the topics and the users                                        |
| `PUT /topics/{topic}/subscribers/{user}`    | Subscribe a user to a topic                                      |
| `DELETE /topics/{topic}/subscribers/{user}` | Unsubscribe                                                      |
| `GET /topics/{topic}/subscribers`       | List a topic's subscribers                                           |

```bash
mvn -pl rest-message-service exec:java          # http://localhost:8080 (set PORT to change it)
curl -X POST localhost:8080/messages -H "Content-Type: application/json" \
     -d '{"sender":"alice","topic":"sports","content":"Hello"}'
```

The first version used global static state and `GET` requests for deletes. It was rewritten as a REST API with an
in-memory store that has no global state.

## Multithreaded chat

A server that handles each client in its own thread, and Swing clients with a login window and a contact list.
Users and passwords are kept in a text file in the server's working directory (`Users.txt`).

```bash
mvn -pl multithreaded-chat compile
cd multithreaded-chat/data
java -cp ../target/classes chat.Main_Server_           # the server, port 8818
java -cp ../target/classes chat.Login_GUI_Frame        # start as many clients as you like
```

Demo accounts: `alice` / `alice123`, `bob` / `bob123`, `carol` / `carol123`.

The clients speak a simple text protocol, which the tests use directly:

| Command                    | What it does                                              |
| -------------------------- | --------------------------------------------------------- |
| `Reg <user> <password>`    | Register a new user                                       |
| `login <user> <password>`  | Log in; other users are told you are online               |
| `msg <user> <text>`        | Send a private message                                    |
| `msg all <text>`           | Send to everyone logged in (rude words get the sender kicked out) |
| `join #topic`, `leave #topic`, `msg #topic <text>` | Join or leave a topic and message it |
| `logoff`                   | Disconnect                                                |

While writing the tests, two server bugs were found and fixed: a client that disconnected without logging off stayed
in the server's list, and the next message to it crashed the sender's thread; and one client that had connected but
not logged in could crash everyone's messages.

## RMI peer-to-peer system

Each peer keeps a set of `<key, value>` pairs, connects to other peers, and can `Find` and `Store` pairs across the
group, with replication, failure counting and response-time measurement. The peers are console programs.

```bash
mvn -pl rmi-peer-to-peer compile
cd rmi-peer-to-peer/target/classes
rmiregistry &                      # the RMI registry on port 1099
java p2p.PeerMain                  # one terminal per peer
```

The networked peer operations need several terminals and were **not run automatically**. The tests cover the binary
tree (`Graph_Representation`) that each peer uses to hold its graph: insert, size, in-order print, remove (leaf, one
child, two children, root) and search. Testing found and fixed two bugs there: searching always went to the right
child, and searching for a missing key crashed after printing "Not found.".

## Notes

- Student assignments keep their original style. The original repositories were a mix of Eclipse projects with
  bundled jar files and compiled classes, which were replaced by Maven builds.
- The instructors' assignment descriptions are not included.

## License

[MIT](LICENSE)