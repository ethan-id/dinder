package onetoone.Likes;

import onetoone.Users.User;
import onetoone.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LikedController {
    @Autowired
    LikeRepository likeRepository;

    @PutMapping(path="/liked")
    public User findingwhatwasliked() {
        User user = new User();
        user.setNewLike(new Liked("Cornbread"));
        user.setNewLike(new Liked("Aztecas"));
        user.setNewLike(new Liked("Tropical Smoothie Cafe"));
        return user;
    }


}
