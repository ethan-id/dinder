package onetoone.Statistics;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Statistic  {
    @Id
    @GeneratedValue
    long id;

    public int totalUsers;
    public int totalLikes;
    public int totalFavorites;



    Statistic(){
        this.totalUsers = 0;
        this.totalLikes = 0;
        this.totalFavorites = 0;

    }

}
