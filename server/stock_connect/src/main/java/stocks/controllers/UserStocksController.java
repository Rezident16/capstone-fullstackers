package stocks.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stocks.data.UserStocksRepository;
import stocks.models.UserStock;

@RestController
@RequestMapping("/api/user-stocks")
@CrossOrigin
public class UserStocksController {

    private final UserStocksRepository repository;

    public UserStocksController(UserStocksRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody UserStock userStock) {
        UserStock result = repository.add(userStock);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userStockId}")
    public ResponseEntity<Void> delete(@PathVariable int userStockId) {
        if (repository.delete(userStockId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
