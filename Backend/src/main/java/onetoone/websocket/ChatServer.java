package onetoone.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import onetoone.Favorites.Favorite;
import onetoone.Favorites.FavoriteRepository;
import onetoone.Likes.LikeRepository;
import onetoone.Likes.Liked;
import onetoone.Requests.Request;
import onetoone.Requests.RequestRepository;
import onetoone.Restaurants.Restaurant;
import onetoone.Restaurants.RestaurantRepository;
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
import java.util.Objects;
import java.util.Set;


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

    private static UserRepository userRepository;
    private static RestaurantRepository restaurantRepository;
    private static LikeRepository likeRepository;
    private static FavoriteRepository favoriteRepository;
    private static RequestRepository requestRepository;

    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;
    }
    @Autowired
    public void setRestaurantRepository(RestaurantRepository restrepo) {
        restaurantRepository = restrepo;
    }
    @Autowired
    public void setLikeRepository(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }
    @Autowired
    public void setFavoriteRepository(FavoriteRepository favoriteRepository){
        this.favoriteRepository = favoriteRepository;}

    @Autowired
    public void setRequestRepository(RequestRepository repo) {
        requestRepository = repo;
    }
    /*
     * Grabs the MessageRepository singleton from the Spring Application
     * Context.  This works because of the @Controller annotation on this
     * class and because the variable is declared as static.
     * There are other ways to set this. However, this approach is
     * easiest.
     */
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
        User user;
        try {
            user = userRepository.findByUsername(username);
        }
        catch (Exception e) {
            broadcast("user does not exist");
            return;
        }

        // Direct message to a user using the format "@username <message>"
        if(message.contains("invite@")){
            // map current group session with username
            groupSessionUsernameMap.putIfAbsent(session, username);
            // map current group username with session
            groupUsernameSessionMap.putIfAbsent(username, session);

            String usernameToAdd = message.substring(7);    //@username and get rid of @
            User userToAdd = new User();
            try {
                userToAdd = userRepository.findByUsername(usernameToAdd);
            }
            catch (Exception e) {
                broadcast("user does not exist");
                return;
            }
            if (!user.isPlus() && (groupSessionUsernameMap.size() >= 2 || groupUsernameSessionMap.size() >= 2)) {
                return;
            }
            for (Request request : user.getRequests()) {
                if (request.getParameter().equals("group") && request.getMessage().substring(27).contains(usernameToAdd)  && !request.getStatus()) {
                    for (Request friendsRequests : userToAdd.getRequests()) {
                        if (friendsRequests.getParameter().equals("group") && !friendsRequests.getStatus() && friendsRequests.getMessage().substring(0, friendsRequests.getMessage().indexOf(' ')).equals(username)) {
                            groupSessionUsernameMap.putIfAbsent(session, username);
                            groupUsernameSessionMap.putIfAbsent(username, session);
                            groupSessionUsernameMap.putIfAbsent(session, usernameToAdd);
                            groupUsernameSessionMap.putIfAbsent(usernameToAdd, session);
                            requestRepository.delete(request);
                            requestRepository.delete(friendsRequests);
                            groupBroadcast(usernameToAdd + " has joined the group!");
                            return;
                        }
                    }
                }
            }
            Request invitedGroupRequest = new Request("group", userToAdd, user.getUsername() + " invited you to Dinder!");
            requestRepository.save(invitedGroupRequest);
            userToAdd.setNewRequest(invitedGroupRequest);
            userRepository.save(userToAdd);
            Request creatorGroupRequest = new Request("group", user, "You requested to Dinder with " + usernameToAdd + " with ID " + invitedGroupRequest.getId());
            requestRepository.save(creatorGroupRequest);
            user.setNewRequest(creatorGroupRequest);
            userRepository.save(user);
            sendMessageToPArticularUser(username, "You requested to Dinder with " + usernameToAdd);
            sendMessageToPArticularUser(usernameToAdd, username + " invited you to Dinder!");
           // Request request = new Request(message, userToAdd);

//
//            // map current group session with username
//            groupSessionUsernameMap.putIfAbsent(session, usernameToAdd);
//            // map current group username with session
//            groupUsernameSessionMap.putIfAbsent(usernameToAdd, session);
        }

        //Sending a friend request
        if (message.contains("request@")) {
            User potentialFriend = userRepository.findByUsername(message.substring(8));
            if (!user.getFriends().isEmpty()) {
                for (User friend : user.getFriends()) {
                    if (potentialFriend.getUsername().equals(friend.getUsername())) {
                        sendMessageToPArticularUser(username, friend.getUsername() + " is already your friend");
                    }
                }
                for (Request request : user.getRequests()) {
                    if (request.getInvitedUser().equals(potentialFriend.getUsername()) && request.getParameter().equals("friend")) {
                        sendMessageToPArticularUser(username, "There is already a pending friend request for you and " + request.getInvitedUser());
                    }
                }
            }
            Request invitedRequest = new Request("friend", potentialFriend, user.getUsername() + " sent you a friend request");
            potentialFriend.setNewRequest(invitedRequest);
            userRepository.save(potentialFriend);
            requestRepository.save(invitedRequest);
            Request creatorRequest = new Request("friend", user, "You sent a friend request to " + potentialFriend.getUsername() + " with ID " + invitedRequest.getId());
            user.setNewRequest(creatorRequest);
            userRepository.save(user);
            requestRepository.save(creatorRequest);
            sendMessageToPArticularUser(user.getUsername(), "You sent a friend request to " + potentialFriend.getUsername());
            sendMessageToPArticularUser(potentialFriend.getUsername(), user.getUsername() + " sent you a friend request!");
        }
//        else { // Message to whole chat
//            broadcast(username + ": " + message);
//        }
        if(message.contains("leave")){
            sendMessageToPArticularUser(username,"Thanks for Dindering!");
            groupUsernameSessionMap.remove(username);
            groupSessionUsernameMap.remove(session);
            userRepository.findByUsername(username).clearLikes();
            userRepository.save(userRepository.findByUsername(username));
        }

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
        if (message.contains("@") && message.contains("like")) {
            try {
                user = userRepository.findByUsername(username);
            }
            catch (Exception e) {
                logger.info("[onError]" + username + ": " + "Could not find username in database");
            }
            String[] newMessage = message.split("@");
            if (newMessage[0].equals("like")) {
                Liked like = new Liked(newMessage[1]);
                like.setUser(Objects.requireNonNull(user));
                Objects.requireNonNull(user).setNewLike(like);
                likeRepository.save(like);
                userRepository.save(user);
            }
            int like_count = 100000000;
            User userWithLeastLikes = new User();
            if (groupUsernameSessionMap.size() >= 2) {
                for (Map.Entry<String, Session> GroupMember : groupUsernameSessionMap.entrySet()) {
                    if (userRepository.findByUsername(GroupMember.getKey()).getLikes().size() < like_count) {
                        userWithLeastLikes = userRepository.findByUsername(GroupMember.getKey());
                    }
                }
                if (userWithLeastLikes.getUsername() != null) {
                    for (Liked likedRestaurant : userWithLeastLikes.getLikes()) {
                        int numberOfLikes = 0;
                        for (Map.Entry<String, Session> GroupMember : groupUsernameSessionMap.entrySet()) {
                            for (Liked like : userRepository.findByUsername(GroupMember.getKey()).getLikes()) {
                                if (like.getName().equals(likedRestaurant.getName())) {
                                    numberOfLikes++;
                                    if (numberOfLikes == groupUsernameSessionMap.size()) {
                                        break;
                                    }
                                }
                            }
                        }
                        if (numberOfLikes == groupUsernameSessionMap.size()) {
                            for (Map.Entry<String, Session> GroupMember : groupUsernameSessionMap.entrySet()) {
                                sendMessageToPArticularUser(GroupMember.getKey(), "Match@" +  newMessage[1]);
                                Favorite favorite = new Favorite(newMessage[1]);
                                userRepository.findByUsername(GroupMember.getKey()).addFavorite(favorite);
                                userRepository.save(userRepository.findByUsername(GroupMember.getKey()));
                            }
                            numberOfLikes = 0;
                            break;
                        }
                        numberOfLikes = 0;
                    }
                }
            }
            else {
                sendMessageToPArticularUser(username,"Match@" + newMessage[1]);
                Favorite favorite = new Favorite(newMessage[1]);
                favorite.setUser(Objects.requireNonNull(user));
                userRepository.findByUsername(username).addFavorite(favorite);
                favoriteRepository.save(favorite);
                userRepository.save(userRepository.findByUsername(username));
            }
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

        groupSessionUsernameMap.remove(session);
        groupUsernameSessionMap.remove(username);

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
        reset();
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
        }
    }

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
    private void reset(){
            for (Map.Entry<String, Session> GroupMember : groupUsernameSessionMap.entrySet()) {
                Set<Liked> userLikes = userRepository.findByUsername(GroupMember.getKey()).getLikes();
                likeRepository.deleteAll(userLikes);
                userLikes.clear();
                userRepository.save(userRepository.findByUsername(GroupMember.getKey()));
                groupUsernameSessionMap.clear();
                groupSessionUsernameMap.clear();
            }
    }
}