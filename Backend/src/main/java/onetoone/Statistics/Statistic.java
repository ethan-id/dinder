package onetoone.Statistics;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Statistic  {
    @Id
    @GeneratedValue
    long id;

    public static int totalUsers;
    public static int totalLikes;
    public static int totalFavorites;



    public Statistic(){
        totalUsers = 0;
        totalLikes = 0;
        totalFavorites = 0;

    }

}
