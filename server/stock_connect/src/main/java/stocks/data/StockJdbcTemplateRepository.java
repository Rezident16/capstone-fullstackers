package stocks.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import stocks.data.mappers.StockMapper;
import stocks.models.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class StockJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public StockJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Stock seeOne(int stockId) {
        final String sql = "SELECT * FROM stocks WHERE stock_id = ?";
        return jdbcTemplate.query(sql, new StockMapper(), stockId).stream()
                .findFirst()
                .orElse(null);
    }

    public List<Stock> seeAll() {
        final String sql = "SELECT * FROM stocks";
        return jdbcTemplate.query(sql, new StockMapper());
    }

    public boolean update(Stock stock) {
        final String sql = "UPDATE stocks SET stock_name = ?, stock_description = ?, ticker = ? WHERE stock_id = ?";
        return jdbcTemplate.update(sql, stock.getStockName(), stock.getDescription(), stock.getTicker(), stock.getStockId()) > 0;
    }

    public boolean delete(int stockId) {
        jdbcTemplate.update("DELETE FROM likes WHERE message_id IN (SELECT message_id FROM message WHERE stock_id = ?)", stockId);
        jdbcTemplate.update("DELETE FROM message WHERE stock_id = ?", stockId);
        jdbcTemplate.update("DELETE FROM user_stocks WHERE stock_id = ?", stockId);
        return jdbcTemplate.update("DELETE FROM stocks WHERE stock_id = ?", stockId) > 0;
    }

    public Stock add(Stock stock) {
        final String sql = "INSERT INTO stocks (stock_name, stock_description, ticker) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, stock.getStockName());
                ps.setString(2, stock.getDescription());
                ps.setString(3, stock.getTicker());
                return ps;
            }
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        stock.setStockId(keyHolder.getKey().intValue());
        return stock;
    }
}
