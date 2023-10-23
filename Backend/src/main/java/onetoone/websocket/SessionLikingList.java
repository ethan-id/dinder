package onetoone.websocket;


import javax.websocket.Session;

public class SessionLikingList {
    private String username;
    private boolean likeValue;
    private String id;

    public SessionLikingList(String username, boolean likeValue, String id) {
        this.username = username;
        this.likeValue = likeValue;
        this.id = id;
    }

    public boolean getLikeValue() { return likeValue; }
    public String getId() { return id; }
    public String getUsername() { return username; }
    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setLikeValue(Boolean likeValue) {this.likeValue = likeValue; }

}
