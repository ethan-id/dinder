package onetoone.Requests;

import onetoone.Users.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String parameter;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User invitedUser;
    private String creator;
    private boolean status;
    private int invitedUserId;


    public Request(String parameter, String creator, User invitedUser) {
        this.parameter = parameter;
        this.invitedUser = invitedUser;
        this.status = true;
        this.creator = creator;
        this.invitedUserId = invitedUser.getId();
    }

    public Request() {}

    public int getId() {
        return id;
    }

    public String getParameter() {
        return parameter;
    }

    public String getInvitedUser() {
        return invitedUser.getUsername();
    }

    public boolean getStatus() {
        return status;
    }

    public String getCreator() { return creator; }

    public int getInvitedUserId() { return invitedUserId; }
    public void setInvitedUserId(int id) { this.invitedUserId = id; }

    public void setId(int id) {
        this.id = id;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    public void setStatus(boolean status) { this.status = status; }
    public void setMessage(String creator) {this.creator = creator; }

}
