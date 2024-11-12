package stocks.domain;

import org.springframework.stereotype.Service;
import stocks.data.LikeRepository;
import stocks.data.MessageRepository;
import stocks.data.UserRepository;
import stocks.models.Like;
import stocks.models.Message;
import stocks.models.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikeService {
    private final LikeRepository repository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public LikeService(LikeRepository repository, UserRepository userRepository, MessageRepository messageRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }


    public List<Like> findAll() {
        return repository.findAll();
    }

    public Like findById(int likeId) {
        return repository.findById(likeId);
    }

    public List<Like> findByUserId(int userId) {
        return repository.findByUserId(userId);
    }

    public List<Like> findByMessageId(int messageId) {
        return repository.findByMessageId(messageId);
    }

    public Result<Like> add(Like like) {
        Result<Like> result = validate(like);
        if (!result.isSuccess()) {
            return result;
        }

        if (like.getLikeId() != 0) {
            result.addMessage("Like Id cannot be set for 'add' operation", ResultType.INVALID);
            return result;
        }

        like = repository.add(like);
        result.setPayload(like);
        return result;
    }

    public Result<Like> update(Like like) {
        Result<Like> result = validate(like);
        if (!result.isSuccess()) {
            return result;
        }
        if (like.getLikeId() <= 0) {
            result.addMessage("likeId cannot be set for 'update' operation", ResultType.INVALID);
            return result;
        }
        if (!repository.update(like)) {
            String msg = String.format("likeId: %s not found", like.getLikeId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    public boolean delete(int likeId) {
        return repository.delete(likeId);
    }

    private Result<Like> validate(Like like) {
        Result<Like> result = new Result<>();

        if (like == null) {
            result.addMessage("Like cannot be null", ResultType.INVALID);
            return result;
        }

        // checking if user id of the like exists in the users

        if (userRepository.findById(like.getUserId()) == null) {
            result.addMessage("User Id  of the like should exist", ResultType.INVALID);
        }

        // checking if message id of the like exists in messages

        if (messageRepository.findById(like.getMessageId()) == null) {
            result.addMessage("Message Id of the like should exist", ResultType.INVALID);
        }

        return result;
    }

}
