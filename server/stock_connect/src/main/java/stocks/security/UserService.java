package stocks.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stocks.data.UserRepository;
import stocks.domain.Result;
import stocks.domain.ResultType;
import stocks.domain.Validations;
import stocks.models.AppUser;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository repository;

    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    // findAll
    public List<AppUser> findAll() {
        return repository.findAll();
    }

    // read by id
    public AppUser findByID(int id) {
        return repository.findById(id);
    }
    // read by username
    public AppUser findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public AppUser findByEmail(String email) {
        return repository.findByEmail(email);
    }

    // create
    public Result<AppUser> add(AppUser appUser) {
        Result<AppUser> result = validate(appUser);
        if (!result.isSuccess()) {
            return result;
        }

        if (appUser.getUserId() != 0) {
            result.addMessage("userId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        appUser = repository.add(appUser);
        result.setPayload(appUser);
        return result;
    }

    // update
    public Result<AppUser> update(AppUser appUser) {
        Result<AppUser> result = validate(appUser);
        if (!result.isSuccess()) {
            return result;
        }

        if (appUser.getUserId() <= 0) {
            result.addMessage("userId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(appUser)) {
            String msg = String.format("userId: %s, not found", appUser.getUserId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;

    }

    // delete
    public boolean deleteById(int userId) {
        return repository.deleteById(userId);
    }

    private Result<AppUser> validate (AppUser user) {
        Result<AppUser> result = new Result<>();

        if (user == null) {
            result.addMessage("User cannot be null", ResultType.INVALID);
            return result;
        }
        if (user.getFirstName().length() > 30) {
            result.addMessage("First Name should be 30 characters long or less", ResultType.INVALID);
        }

        if (user.getLastName().length() > 30) {
            result.addMessage("Last Name should be 30 characters long or less", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(user.getUsername()) || user.getUsername().length() > 25) {
            result.addMessage("Username is required and should be 25 characters or less", ResultType.INVALID);
        }
//        password

        if (Validations.isNullOrBlank(user.getPassword()) || user.getPassword().length() > 100 || user.getPassword().length() < 8) {
            result.addMessage("Password is required and should be between 8-100 characters long", ResultType.INVALID);
        }
//        email
        if (Validations.isNullOrBlank(user.getEmail())) {
            result.addMessage("Email is required", ResultType.INVALID);
        }

        if (Validations.isValidEmail(user.getEmail())) {
            result.addMessage("Email is not valid", ResultType.INVALID);
        }



        return result;
    }


}
