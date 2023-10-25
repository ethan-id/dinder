package onetoone.Likes;

import onetoone.Users.User;
import onetoone.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LikedController {
    @Autowired
    LikeRepository likeRepository;
}
