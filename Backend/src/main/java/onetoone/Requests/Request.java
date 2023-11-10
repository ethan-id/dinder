package onetoone.Requests;

import onetoone.Users.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String parameter;

    private User creator;

    private User invitedUser;

    public Request(String parameter, User creator, User invitedUser) {
        this.parameter = parameter;
        this.creator = creator;
        this.invitedUser = invitedUser;
    }

    public int getId() {
        return id;
    }

    public String getParameter() {
        return parameter;
    }

    public String getCreator() {
        return creator.getUsername();
    }

    public String getInvitedUser() {
        return invitedUser.getUsername();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setCreatedBy(User creator) {
        this.creator = creator;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }
}
