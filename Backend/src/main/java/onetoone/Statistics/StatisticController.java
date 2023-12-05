package onetoone.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class StatisticController {

    StatisticRepository statisticRepository;


    @PostMapping(path = "/add/{Statistic}")
    public String addStatistic(@PathVariable Statistic statistic){
        if (statistic == null)
            return "failure";

        statisticRepository.save(statistic);
        return "success";
    }
    @GetMapping(path = "/stats/getTotalLikes")
    public int getTotalLikes() {
        return Statistic.totalLikes;
    }
    @GetMapping(path = "/stats/getTotalUsers")
    public int getTotalUsers() {
        return Statistic.totalUsers;
    }
    @GetMapping(path = "/stats/getTotalFavorites")
    public int getTotalFavorites() {
        return Statistic.totalFavorites;
    }
    @GetMapping(path = "/stats/getAvgSwipesBeforeMatch")
    public double getAvgSwipesBeforeMatch() {
        return Statistic.avgSwipesBeforeMatch;
    }
    @GetMapping(path = "/stats/getAvgLikesPerMatch")
    public double getAvgLikesPerMatch() {
        return Statistic.avgLikesPerMatch;
    }
    @GetMapping(path = "/stats/getAvgSwipesBeforeLike")
    public double getAvgSwipesBeforeLike() {
        return Statistic.avgSwipesBeforeLike;
    }
    @GetMapping(path = "/stats/getAvgLikesPerUser")
    public double getAvgLikesPerUser() {
        return Statistic.avgLikesPerUser;
    }
    @GetMapping(path = "/stats/getAvgMatchesPerUser")
    public double getAvgMatchesPerUser() {
        return Statistic.avgMatchesPerUser;
    }
}
