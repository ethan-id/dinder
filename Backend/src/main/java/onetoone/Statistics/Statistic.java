package onetoone.Statistics;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
public class Statistic  {
    @Id
    @GeneratedValue
    long id;

    public static int totalUsers;
    public static int totalLikes;
    public static int totalFavorites;
    public static int totalSwipes;
    public static int totalMatches;

    public static int likesBeforeMatch;

    public static double avgSwipesBeforeMatch;
    public static double avgLikesPerMatch;
    public static double avgSwipesBeforeLike;
    public static double avgLikesPerUser;
    public static double avgMatchesPerUser;




    public Statistic(){
        totalUsers = 0;
        totalLikes = 0;
        totalFavorites = 0;
    }

    public static void calculate() {
        avgLikesPerMatch = ( (double) totalMatches / totalLikes);
        avgSwipesBeforeMatch = ( (double) totalSwipes / totalMatches);
        avgSwipesBeforeLike = ( (double) totalSwipes / totalLikes);
        avgLikesPerUser = ( (double) totalLikes / totalUsers);
        avgMatchesPerUser = ( (double) totalMatches / totalUsers);

    }
}
