package stocks.domain;

import org.springframework.stereotype.Service;
import stocks.data.StockRepository;
import stocks.models.Stock;

import java.util.List;

@Service
public class StockService {
    private final StockRepository repository;

    public StockService(StockRepository repository) {
        this.repository = repository;
    }

    public Stock seeOne(int stockId) {
        return repository.seeOne(stockId);
    }

    public List<Stock> seeAll() {
        return repository.seeAll();
    }

    public Result<Stock> add(Stock stock) {
        Result<Stock> result = validate(stock);
        if (!result.isSuccess()) {
            return result;
        }

        Stock addedStock = repository.add(stock);
        result.setPayload(addedStock);
        return result;
    }

    public Result<Stock> update(Stock stock) {
        Result<Stock> result = validate(stock);
        if (!result.isSuccess()) {
            return result;
        }

        System.out.println(stock.getStockName());
        System.out.println(stock.getDescription());
        System.out.println(stock.getTicker());
        System.out.println(stock.getStockId());


        if (stock.getStockId() <= 0) {
            result.addMessage("stockId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(stock)) {
            String msg = String.format("StockId: %s not found!", stock.getStockId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean delete(int stockId) {
        return repository.delete(stockId);
    }

    public Result<Stock> validate(Stock stock) {
        Result<Stock> result = new Result<>();
        if (stock.getStockName() == null || stock.getStockName().isBlank()) {
            result.addMessage("Stock name is required", ResultType.INVALID);
        }
        if (stock.getDescription() == null || stock.getDescription().isBlank()) {
            result.addMessage("Stock description is required", ResultType.INVALID);
        }
        if (stock.getTicker() == null || stock.getTicker().isBlank()) {
            result.addMessage("Stock ticker is required", ResultType.INVALID);
        }
        return result;
    }
}
