package onetoone.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import onetoone.Likes.Liked;
import onetoone.Requests.Request;
import onetoone.Restaurants.Restaurant;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Vivek Bengre
 *
 */

@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String username;
    private String passkey;
    private boolean vegan;
    private boolean vegitarian;
    private boolean halal;
    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    private Set<Liked> likes;

    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    private Set<Request> requests;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_restaurant_favorite",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private Set<Restaurant> favoriteRestaurants;

    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="friends_with",
            joinColumns={@JoinColumn(name="person_id")},
            inverseJoinColumns={@JoinColumn(name="friend_id")})
    @JsonIgnore
    private Set<User> friends = new HashSet<User>();

    @ManyToMany(mappedBy="friends")
    @JsonIgnore
    private Set<User> friendsOf = new HashSet<User>();


    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */



    public User(String name, String username, String passkey) {
        this.name = name;
        this.username = username;
        this.passkey = passkey;
        this.vegan = false;
        this.vegitarian = false;
        this.halal = false;
        this.likes = new HashSet<Liked>();
        this.favoriteRestaurants = new HashSet<Restaurant>();
        this.friends = new HashSet<User>();
        this.friendsOf = new HashSet<User>();
        this.requests = new HashSet<Request>();
    }

    public User() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isHalal() {
        return halal;
    }

    public void setHalal(boolean halal) {
        this.halal = halal;
    }

    public boolean isVegitarian() { return vegitarian; }

    public void setVegitarian(boolean vegitarian) {
        this.vegitarian = vegitarian;
    }

    public Set<Liked> getLikes() { return likes; }
    public Set<Request> getRequests() {return requests; }
    public void setNewLike(Liked like) { likes.add(like); }
    public void setNewRequest(Request request) { requests.add(request); }
    public void clearLikes(){
        getLikes().clear();
    }
    public Set<Restaurant> getFavoriteRestaurants(){
        return favoriteRestaurants;
    }

    public void addFavorite(Restaurant restaurant){
        favoriteRestaurants.add(restaurant);
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void removeFriend(User friend) { if (friend != null) { friends.remove(friend); } }

    public User findFriendByUsername(String friend) {
        if (friend != null) {
            for (User user : friends) {
                if (user.getUsername().equals(friend)) {
                    return user;
                }
            }
            return null;
        }
        return null;
    }

    public Set<String> getAllFriends() {
        Set response = new HashSet<String>();
        for (User user : friends) {
            response.add(Objects.requireNonNull(user.getUsername()));
        }
        return response; }



    //    public void setFavoriteRestaurants(Set<Restaurant> favorites){ this.favoriteRestaurants = favorites;}
}
