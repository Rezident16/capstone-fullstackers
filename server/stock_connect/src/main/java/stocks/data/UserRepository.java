package stocks.data;


import org.springframework.transaction.annotation.Transactional;
import stocks.models.User;
import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User findById(int userId);

    User findByUsername(String username);

    User findByEmail(String email);

    boolean add(User user);

    boolean update(User user);

    boolean deleteById(int locationId);
}
