package stocks.data;

import stocks.data.mappers.StockMapper;
import stocks.models.Stock;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class StockJdbcTemplateRepository implements StockRepository {
    private final JdbcTemplate jdbcTemplate;

    public StockJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Stock seeOne(int stockId) {
        final String sql = "SELECT * FROM stocks WHERE stock_id = ?";

        return jdbcTemplate.query(sql, new StockMapper(), stockId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Stock> seeAll() {
        final String sql = "SELECT * FROM stocks";
        return jdbcTemplate.query(sql, new StockMapper());
    }

    @Override
    public boolean update(Stock stock) {
        final String sql = "UPDATE stocks SET stock_name = ?, description = ?, ticker = ? WHERE stock_id = ?";
        return jdbcTemplate.update(sql, stock.getStockName(), stock.getDescription(), stock.getTicker(),
                stock.getStockId()) > 0;
    }

    @Override
    public boolean delete(int stockId) {
        return jdbcTemplate.update("delete from stocks where stock_id = ?", stockId) > 0;
    }

    @Override
    public Stock add(Stock stock) {
        final String sql = "INSERT INTO stocks (stock_name, description, ticker) VALUES (?, ?, ?) RETURNING stock_id";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql, new String[]{"stock_id"});
            ps.setString(1, stock.getStockName());
            ps.setString(2, stock.getDescription());
            ps.setString(3, stock.getTicker());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        stock.setStockId(keyHolder.getKey().intValue());
        return stock;
    }
}
