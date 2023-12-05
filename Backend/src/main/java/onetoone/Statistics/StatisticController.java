package onetoone.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StatisticController {

    StatisticRepository statisticRepository;



    @GetMapping(path = "/stats/get/Likes")
    public int getTotalLikes() {
        return Statistic.totalLikes;
    }
    @GetMapping(path = "/stats/get/Users")
    public int getTotalUsers() {
        return Statistic.totalUsers;
    }
    @GetMapping(path = "/stats/get/Favorites")
    public int getTotalFavorites() {
        return Statistic.totalFavorites;
    }
    @GetMapping(path = "/stats/get/SwipesBeforeMatch")
    public double getAvgSwipesBeforeMatch() {
        return Statistic.avgSwipesBeforeMatch;
    }
    @GetMapping(path = "/stats/get/LikesPerMatch")
    public double getAvgLikesPerMatch() {
        return Statistic.avgLikesPerMatch;
    }
    @GetMapping(path = "/stats/get/SwipesBeforeLike")
    public double getAvgSwipesBeforeLike() {
        return Statistic.avgSwipesBeforeLike;
    }
    @GetMapping(path = "/stats/get/LikesPerUser")
    public double getAvgLikesPerUser() {
        return Statistic.avgLikesPerUser;
    }
    @GetMapping(path = "/stats/get/MatchesPerUser")
    public double getAvgMatchesPerUser() {
        return Statistic.avgMatchesPerUser;
    }
}
