package onetoone.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class StatisticController {

    StatisticRepository statisticRepository;


    @PostMapping("/addStatistic")
    public String addStatistic(@RequestBody Statistic statistic){
        if (statistic == null)
            return "failure";

        statisticRepository.save(statistic);
        return "success";
    }
}
