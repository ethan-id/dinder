package onetoone.websocket;

import onetoone.Likes.Liked;
import onetoone.Users.User;
import onetoone.Users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;



/**
 * Represents a WebSocket chat server for handling real-time communication
 * between users. Each user connects to the server using their unique
 * username.
 *
 * This class is annotated with Spring's `@ServerEndpoint` and `@Component`
 * annotations, making it a WebSocket endpoint that can handle WebSocket
 * connections at the "/chat/{username}" endpoint.
 *
 * Example URL: ws://localhost:8080/chat/username
 *
 * The server provides functionality for broadcasting messages to all connected
 * users and sending messages to specific users.
 */
@ServerEndpoint("/chat/{username}")
@Component
public class ChatServer {

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map < Session, String > sessionUsernameMap = new Hashtable < > ();
    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();

    private static Map < Session, String > groupSessionUsernameMap = new Hashtable < > ();
    private static Map < String, Session > groupUsernameSessionMap = new Hashtable < > ();

//    private static ArrayList <SessionLikingList> groupSessionLikingList = new ArrayList<SessionLikingList>();


    private static UserRepository userRepo;

    /*
     * Grabs the MessageRepository singleton from the Spring Application
     * Context.  This works because of the @Controller annotation on this
     * class and because the variable is declared as static.
     * There are other ways to set this. However, this approach is
     * easiest.
     */
    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepo = repo;
    }
    // server side logger
    private final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param username username specified in path parameter.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        // server side log
        logger.info("[onOpen] " + username);

        // Handle the case of a duplicate username
//        if (usernameSessionMap.containsKey(username)) {
//            session.getBasicRemote().sendText("Username already exists");
//            session.close();
//        }

            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);

            // send to the user joining in
//            sendMessageToPArticularUser(username, "Welcome to the chat server, "+username);
//
            // send to everyone in the chat
            broadcast("User: " + username + " has Joined the Chat");

    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param message The message received from the client.
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        // get the username by session
        String username = sessionUsernameMap.get(session);
        // server side log
        logger.info("[onMessage] " + username + ": " + message);

        // Direct message to a user using the format "@username <message>"
        if(message.contains("invite@")){
            if(!(groupUsernameSessionMap.containsKey(username))){
                // map current group session with username
                groupSessionUsernameMap.put(session, username);
                // map current group username with session
                groupUsernameSessionMap.put(username, session);
            }
            String usernameToAdd = message.substring(7);    //@username and get rid of @
            if(!(groupUsernameSessionMap.containsKey(usernameToAdd))){
                // map current group session with username
                groupSessionUsernameMap.put(session, username);
                // map current group username with session
                groupUsernameSessionMap.put(username, session);
                sendMessageToPArticularUser(usernameToAdd, "invitee@"+usernameToAdd);
                sendMessageToPArticularUser(username, "invited@"+usernameToAdd);
            }

        }
//        else { // Message to whole chat
//            broadcast(username + ": " + message);
//        }
        if (message.startsWith("@")) {

            // split by space
            String[] split_msg = message.split("\\s+");

            // Combine the rest of message
            StringBuilder actualMessageBuilder = new StringBuilder();
            for (int i = 1; i < split_msg.length; i++) {
                actualMessageBuilder.append(split_msg[i]).append(" ");
            }
            String destUserName = split_msg[0].substring(1);    //@username and get rid of @
            String actualMessage = actualMessageBuilder.toString();
            sendMessageToPArticularUser(destUserName, "[DM from " + username + "]: " + actualMessage);
            sendMessageToPArticularUser(username, "[DM from " + username + "]: " + actualMessage);
        }
//        else { // Message to whole chat
//            broadcast(username + ": " + message);
//        }
        User user = userRepo.findByUsername(username);
        if (message.contains("@") && message.contains("like")) {
            String[] a = message.split("@");
            if (a[0].equals("like")) {
                // groupSessionLikingList.add(new SessionLikingList(username, true, a[1]));
                Liked c = new Liked(a[1],user);
                try {
                    user.setNewLike(c);
                    logger.info("Set new like");
                } catch (Exception e) {
                    System.out.println("Could not find User by their Username");
                }
            }
//            int like_count = 0;
//            User userWithMostLikes = new User();
//            int numberOfLikes = 0;
//            for (Map.Entry<String, Session> barney : groupUsernameSessionMap.entrySet()) {
//                if (userRepo.findByUsername(barney.getKey()).getLikes().size() > like_count) {
//                    userWithMostLikes = userRepo.findByUsername(barney.getKey());
//                }
//            }
//            for (Liked name : userWithMostLikes.getLikes()) {
//                for (Map.Entry<String, Session> barney : groupUsernameSessionMap.entrySet()) {
//                    if (userRepo.findByUsername(barney.getKey()).getLikes().contains(name)) {
//                        numberOfLikes++;
//                        if (numberOfLikes == groupUsernameSessionMap.size()) {
//                            break;
//                        }
//                    }
//                }
//                if (numberOfLikes == groupUsernameSessionMap.size()) {
//                    break;
//                }
//                numberOfLikes = 0;
//            }
        }
    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] " + username);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // send the message to chat
        broadcast(username + " disconnected");
    }

    /**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    private void sendMessageToPArticularUser(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        }
        Session session = usernameSessionMap.get(username);
        if (message.contains("group")) {
            // map current group session with username
            groupSessionUsernameMap.put(session, username);

            // map current group username with session
            groupUsernameSessionMap.put(username, session);
            sendMessageToPArticularUser(username, "[Group " + username + "]: You are in a group ");
        } else { // Message to whole chat
            broadcast(username + ": " + message);
        }
    }
        /**
         *  Jesse newly added here down ---> *Untested*
         */


    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users.
     */
    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }

    private void groupBroadcast(String message) {
        groupSessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }
}