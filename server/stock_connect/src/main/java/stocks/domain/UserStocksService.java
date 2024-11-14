package stocks.domain;

import org.springframework.stereotype.Service;
import stocks.data.UserStocksRepository;
import stocks.models.UserStock;

@Service
public class UserStocksService {

    private final UserStocksRepository repository;

    public UserStocksService(UserStocksRepository repository) {
        this.repository = repository;
    }

    public Result<UserStock> add(UserStock userStock) {
        Result<UserStock> result = validate(userStock);
        if (!result.isSuccess()) {
            return result;
        }

        UserStock addedUserStock = repository.add(userStock);
        if (addedUserStock == null) {
            result.addMessage("Failed to add user stock", ResultType.INVALID);
        } else {
            result.setPayload(addedUserStock);
        }

        return result;
    }

    public Result<Void> delete(int userStockId) {
        Result<Void> result = new Result<>();

        boolean deleted = repository.delete(userStockId);
        if (!deleted) {
            result.addMessage("User stock not found or could not be deleted", ResultType.NOT_FOUND);
        }

        return result;
    }

    private Result<UserStock> validate(UserStock userStock) {
        Result<UserStock> result = new Result<>();

        if (userStock == null) {
            result.addMessage("UserStock cannot be null", ResultType.INVALID);
            return result;
        }

        if (userStock.getUserId() <= 0) {
            result.addMessage("userId must be greater than 0", ResultType.INVALID);
        }

        if (userStock.getStockId() <= 0) {
            result.addMessage("stockId must be greater than 0", ResultType.INVALID);
        }

        return result;
    }
}
