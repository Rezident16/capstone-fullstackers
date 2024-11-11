package stocks.data;

import stocks.models.Stock;

import java.util.List;

public interface StockRepository {
//    read
    Stock seeOne (int stockId);
//    read all
    List<Stock> seeAll();
//    update
    boolean update(Stock stock);
//    delete
    boolean delete(int stockId);
//    create
    Stock add(Stock stock);
}
