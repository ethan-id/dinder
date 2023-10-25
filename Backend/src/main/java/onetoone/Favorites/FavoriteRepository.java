package onetoone.Favorites;

import onetoone.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<User, Long> {
}
