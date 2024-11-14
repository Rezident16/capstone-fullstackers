package stocks.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import stocks.data.UserStocksRepository;
import stocks.models.UserStock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserStocksServiceTest {

    @Autowired
    private UserStocksService service;

    @MockBean
    private UserStocksRepository repository;

    @Test
    void shouldAddUserStock() {
        UserStock userStock = new UserStock();
        userStock.setUserId(1);
        userStock.setStockId(2);

        UserStock mockUserStock = new UserStock();
        mockUserStock.setUserStockId(1);
        mockUserStock.setUserId(1);
        mockUserStock.setStockId(2);

        when(repository.add(userStock)).thenReturn(mockUserStock);

        Result<UserStock> result = service.add(userStock);

        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(mockUserStock, result.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        UserStock userStock = new UserStock();
        userStock.setUserId(0); // Invalid userId
        userStock.setStockId(2);

        Result<UserStock> result = service.add(userStock);
        assertEquals(ResultType.INVALID, result.getType());

        userStock.setUserId(1);
        userStock.setStockId(0); // Invalid stockId
        result = service.add(userStock);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldDeleteUserStock() {
        int userStockId = 1;

        when(repository.delete(userStockId)).thenReturn(true);

        Result<Void> result = service.delete(userStockId);

        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotDeleteWhenNotFound() {
        int userStockId = 99;

        when(repository.delete(userStockId)).thenReturn(false);
        Result<Void> result = service.delete(userStockId);
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }
}
