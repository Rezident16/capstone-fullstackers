package stocks.domain;

import stocks.data.UserRepository;
import stocks.models.User;

import java.util.List;

public class UserService {
    private final UserRepository repository;
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(int userId) {
        return repository.findById(userId);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Result<User> add(User user) {
        Result<User> result = validate(user);
        if(!result.isSuccess()) {
            return result;
        }

        repository.add(user);
        result.setPayload(user);
        return result;
    }

    public Result<User> update(User user) {
        Result<User> result = validate(user);
        if(!result.isSuccess()) {
            return result;
        }

        if(user.getUserId() <= 0) {
            result.addMessage("userId must be above 0", ResultType.INVALID);
        }

        if(!repository.update(user)) {
            String msg = String.format("UserId: %s not found!", user.getUserId());
            result.addMessage(msg, ResultType.INVALID);
        }

        return result;
    }

    public boolean deleteById(int userId) {
        return repository.deleteById(userId);
    }

    public Result<User> validate(User user) {
        Result<User> result = new Result<>();

        if(user.getPassword() == null || user.getPassword().isBlank()) {
            result.addMessage("password is required", ResultType.INVALID);
        }
        if(user.getUsername() == null || user.getUsername().isBlank()) {
            result.addMessage("username is required", ResultType.INVALID);
        }
        if(user.getEmail() == null || user.getEmail().isBlank()) {
            result.addMessage("email is required", ResultType.INVALID);
        }

        return result;
    }
}
