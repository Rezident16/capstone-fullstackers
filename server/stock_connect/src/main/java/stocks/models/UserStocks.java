package stocks.models;

public class UserStocks {
    private int userStockId;
    private int userId;
    private int stockId;

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
