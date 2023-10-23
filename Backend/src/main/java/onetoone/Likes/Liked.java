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

    public Liked(int id, String name){
        this.id = id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
