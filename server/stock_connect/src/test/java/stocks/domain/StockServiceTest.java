package stocks.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import stocks.data.StockRepository;
import stocks.models.Stock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StockServiceTest {

    @Autowired
    StockService service;

    @MockBean
    StockRepository repository;

    @Test
    void shouldNotAddWhenInvalid() {
        // Test when stock name is blank
        Stock stock = makeStock();
        stock.setStockName("   ");
        Result<Stock> result = service.add(stock);
        assertEquals(ResultType.INVALID, result.getType());

        // Test when description is null
        stock = makeStock();
        stock.setDescription(null);
        result = service.add(stock);
        assertEquals(ResultType.INVALID, result.getType());

        // Test when ticker is blank
        stock = makeStock();
        stock.setTicker("   ");
        result = service.add(stock);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldAdd() {
        Stock stock = makeStock();
        Stock mockOut = makeStock();
        mockOut.setStockId(1);

        when(repository.add(stock)).thenReturn(mockOut);

        Result<Stock> result = service.add(stock);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(mockOut, result.getPayload());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Stock stock = makeStock();
        stock.setStockName("");
        Result<Stock> result = service.update(stock);
        assertEquals(ResultType.INVALID, result.getType());

        stock = makeStock();
        stock.setStockId(1);
        stock.setStockName("   ");
        result = service.update(stock);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdate() {
        Stock stock = makeStock();
        stock.setStockId(1);

        when(repository.update(stock)).thenReturn(true);

        Result<Stock> result = service.update(stock);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateWhenNotFound() {
        Stock stock = makeStock();
        stock.setStockId(1);

        when(repository.update(stock)).thenReturn(false);

        Result<Stock> result = service.update(stock);
        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertTrue(result.getMessages().contains("StockId: 1 not found!"));
    }

    @Test
    void shouldDelete() {
        when(repository.delete(1)).thenReturn(true);

        boolean result = service.delete(1);
        assertTrue(result);
        verify(repository).delete(1); // Verify repository delete was called
    }

    @Test
    void shouldNotDeleteWhenNotFound() {
        when(repository.delete(1)).thenReturn(false);

        boolean result = service.delete(1);
        assertFalse(result);
    }

    private Stock makeStock() {
        Stock stock = new Stock();
        stock.setStockName("Test Stock");
        stock.setDescription("This is a test stock description.");
        stock.setTicker("TEST");
        return stock;
    }
}
