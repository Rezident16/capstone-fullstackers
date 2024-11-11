package stocks.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import stocks.models.UserStock;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserStocksJdbcRepositoryTest {

    @Autowired
    UserStocksJdbcRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void add() {
        UserStock userStock = makeUserStock();
        UserStock actual = repository.add(userStock);
        assertNotNull(actual);
        assertEquals(3, actual.getUserStockId());
    }

    @Test
    void addShouldFailWhenUserIdIsInvalid() {
        UserStock userStock = makeUserStock();
        userStock.setUserId(-1); // Invalid user ID
        UserStock actual = repository.add(userStock);
        assertNull(actual);
    }

    @Test
    void addShouldFailWhenStockIdIsInvalid() {
        UserStock userStock = makeUserStock();
        userStock.setStockId(-1); // Invalid stock ID
        UserStock actual = repository.add(userStock);
        assertNull(actual);
    }

    @Test
    void delete() {
        assertTrue(repository.delete(1));
        assertFalse(repository.delete(1));
    }

    @Test
    void deleteShouldFailWhenUserStockIdIsInvalid() {
        assertFalse(repository.delete(-1));
    }

    UserStock makeUserStock() {
        UserStock userStock = new UserStock();
        userStock.setUserId(1);
        userStock.setStockId(1);
        return userStock;
    }
}
