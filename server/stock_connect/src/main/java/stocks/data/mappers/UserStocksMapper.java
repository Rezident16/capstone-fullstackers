package stocks.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import stocks.models.UserStock;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserStocksMapper implements RowMapper<UserStock> {
    @Override
    public UserStock mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserStock userStocks = new UserStock();
        userStocks.setUserStockId(rs.getInt("user_stock_id"));
        userStocks.setUserId(rs.getInt("user_id"));
        userStocks.setStockId(rs.getInt("stock_id"));
        return userStocks;
    }
}
