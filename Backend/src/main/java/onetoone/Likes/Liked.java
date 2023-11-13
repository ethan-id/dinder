package onetoone.Likes;

import onetoone.Users.User;

import javax.persistence.*;

@Entity
//@Table(name="LIKES")
public class Liked {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    public Liked(){

    }

    public Liked(String name){
        this.name = name;
    }

    /**
     * id is likeId
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * name is restaurantId
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setUser(User user) { this.user = user; }


}
