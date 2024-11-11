package stocks.data.mappers;

import stocks.models.Stock;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockMapper implements RowMapper<Stock> {

    @Override
    public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
        Stock stock = new Stock();
        stock.setStockId(rs.getInt("stock_id"));
        stock.setStockName(rs.getString("stock_name"));
        stock.setDescription(rs.getString("description"));
        stock.setTicker(rs.getString("ticker"));
        return stock;
    }
}
