package stocks.data;


import org.springframework.transaction.annotation.Transactional;
import stocks.models.AppUser;
import java.util.List;

public interface UserRepository {
    List<AppUser> findAll();
    AppUser findById(int userId);

    AppUser findByUsername(String username);

    AppUser findByEmail(String email);

    boolean add(AppUser user);

    boolean update(AppUser user);

    boolean deleteById(int locationId);
}
