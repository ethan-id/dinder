package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;



public interface UserRepository extends JpaRepository<User, Long> {
    
    User findById(int id);

    //User findByUsername(String username);

    //@Query("SELECT * FROM user WHERE username = :username"))
    //Flux<User> findByUsername(String username);


    @Transactional
    void deleteById(int id);

    User findByLaptop_Id(int id);

    User findByUsername(String username);

}
