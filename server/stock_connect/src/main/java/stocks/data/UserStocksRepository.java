package stocks.data;

import stocks.models.UserStock;

public interface UserStocksRepository {

    UserStock add (UserStock userStock);
    boolean delete(int userStockId);
}
