package onetoone.Friends;

import onetoone.Users.User;

import javax.persistence.*;

@Entity
//@Table(name="Friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id")
    private User friend;

    public Friend(User friend){
        this.friend = friend;
    }
    public Friend(){}

    public int getId() {
        return id;
    }

    public User getFriend() {
        return friend;
    }


}
