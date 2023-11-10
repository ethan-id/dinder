package onetoone.Requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

    @Autowired
    RequestRepository requestRepository;
}
