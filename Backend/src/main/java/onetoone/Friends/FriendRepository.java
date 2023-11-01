package onetoone.Friends;

import onetoone.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<User, Long> {
}
