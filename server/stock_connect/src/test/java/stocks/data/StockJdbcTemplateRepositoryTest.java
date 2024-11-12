package stocks.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import stocks.models.Stock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StockJdbcTemplateRepositoryTest {

    final static int NEXT_STOCK_ID = 4;

    @Autowired
    StockJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFind() {
        Stock actual = repository.seeOne(1);
        assertNotNull(actual);
        assertEquals(1, actual.getStockId());
    }

    @Test
    void shouldSeeAll() {
        List<Stock> stocks = repository.seeAll();
        assertNotNull(stocks);
        assertTrue(stocks.size() >= 2);
    }

    @Test
    void shouldAdd() {
        Stock stock = makeStock();
        Stock actual = repository.add(stock);
        assertNotNull(actual);
        assertEquals(NEXT_STOCK_ID, actual.getStockId());
    }

    @Test
    void shouldUpdate() {
        Stock stock = makeStock();
        stock.setStockId(1);
        assertTrue(repository.update(stock));
        stock.setStockId(20);
        assertFalse(repository.update(stock));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.delete(2));
        assertFalse(repository.delete(2));
    }

    Stock makeStock() {
        Stock stock = new Stock();
        stock.setStockName("Test Stock");
        stock.setDescription("Test Description");
        stock.setTicker("TST");
        return stock;
    }
}
