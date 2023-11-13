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

    private boolean status;

    @Autowired
    RequestRepository requestRepository;

    public Request(String parameter, User invitedUser) {
        this.parameter = parameter;
        this.invitedUser = invitedUser;
        this.status = true;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    public void setStatus(boolean status) {
        this.status = status;
        if (!this.status) {
            requestRepository.deleteById(this.id);
        }
    }
}
