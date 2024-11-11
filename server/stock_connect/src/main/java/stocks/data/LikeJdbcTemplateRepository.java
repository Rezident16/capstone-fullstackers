package stocks.data;

import org.springframework.jdbc.core.JdbcTemplate;
import stocks.models.Like;

public class LikeJdbcTemplateRepository implements LikeRepository{
    private final JdbcTemplate jdbcTemplate;

    public LikeJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(Like like) {
        return false;
    }

    @Override
    public boolean update(int likeId) {
        return false;
    }

    @Override
    public boolean delete(int likeId) {
        return false;
    }
}
