package stocks.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import stocks.models.UserStock;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserStocksJdbcRepository implements UserStocksRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserStocksJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Object[]> findFavoritesByUserId(int userId) {
        final String sql =
                "SELECT us.user_stock_id, s.stock_id, s.stock_name, s.stock_description, s.ticker " +
                        "FROM user_stocks us " +
                        "INNER JOIN stock s ON us.stock_id = s.stock_id " +
                        "WHERE us.user_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            return new Object[] {
                    rs.getInt("user_stock_id"),
                    rs.getInt("stock_id"),
                    rs.getString("stock_name"),
                    rs.getString("stock_description"),
                    rs.getString("ticker")
            };
        }, userId);
    }



    @Override
    public UserStock add(UserStock userStock) {
        final String sql = "INSERT INTO user_stocks (user_id, stock_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userStock.getUserId());
            ps.setInt(2, userStock.getStockId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        userStock.setUserStockId(keyHolder.getKey().intValue());
        return userStock;
    }

    @Override
    public boolean delete(int userStockId) {
        return jdbcTemplate.update("delete from user_stocks where user_stock_id = ?", userStockId) > 0;
    }
}
