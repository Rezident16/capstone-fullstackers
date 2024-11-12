package stocks.data;

import stocks.models.Like;

import java.util.List;

public interface LikeRepository {
    List<Like> findAll();

    Like findById(int likeId);

    List<Like> findByUserId(int userId);

    List<Like> findByMessageId(int messageId);

    Like add(Like like);

    boolean update(Like like);

    boolean delete(int likeId);
}
