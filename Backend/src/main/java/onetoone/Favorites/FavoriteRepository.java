package onetoone.Favorites;

import onetoone.Likes.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
