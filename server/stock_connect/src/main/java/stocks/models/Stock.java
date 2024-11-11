package stocks.models;

public class Stock {
    private int stockId;
    private String stockName;
    private String stock_description;
    private String ticker;


    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getDescription() {
        return stock_description;
    }

    public void setDescription(String description) {
        this.stock_description = description;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}