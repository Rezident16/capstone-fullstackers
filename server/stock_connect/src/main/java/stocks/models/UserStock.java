package stocks.models;

public class UserStock {
    private int userStockId;
    private int userId;
    private int stockId;

    public UserStock(int userStockId, int userId, int stockId) {
        this.userStockId = userStockId;
        this.userId = userId;
        this.stockId = stockId;
    }

    public UserStock() {}

    public int getUserStockId() {
        return userStockId;
    }

    public void setUserStockId(int userStockId) {
        this.userStockId = userStockId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }
}
