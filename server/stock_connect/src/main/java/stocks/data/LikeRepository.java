package stocks.data;

import stocks.models.Like;

public interface LikeRepository {
    boolean add(Like like);

    boolean update(Like like);

    boolean delete(int likeId);
}
