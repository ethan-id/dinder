package onetoone.Favorites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import onetoone.Users.User;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    @JoinColumn(name = "favoriter_id")
    @JsonIgnore
    private User user;
    String code;

    public Favorite(String code){
        this.code = code;
    }

    public Favorite(){}

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
