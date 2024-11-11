package stocks.data;

import stocks.models.Like;

public interface LikeRepository {
    boolean add(Like like);

    boolean update(int likeId);

    boolean delete(int likeId);
}
