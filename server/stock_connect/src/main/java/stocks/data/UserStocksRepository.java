package stocks.data;

import stocks.models.UserStock;

import java.util.List;

public interface UserStocksRepository {

    List<Object[]> findFavoritesByUserId(int userId);
    UserStock add (UserStock userStock);
    boolean delete(int userStockId);
}
