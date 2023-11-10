package onetoone.Requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    public Request findById(int id);

    public Request findByParameter(String parameter);

    @Transactional
    public void deleteById(int id);
}
