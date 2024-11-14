package stocks.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stocks.data.UserStocksRepository;
import stocks.models.Stock;
import stocks.models.UserStock;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/user-stocks")
public class UserStocksController {

    private final UserStocksRepository repository;

    public UserStocksController(UserStocksRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/favorites/{userId}")
    public ResponseEntity<List<Object[]>> getFavoritesByUserId(@PathVariable int userId) {
        List<Object[]> favorites = repository.findFavoritesByUserId(userId);
        if (favorites.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(favorites, HttpStatus.OK);
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
        boolean isDeleted = repository.delete(userStockId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Success: Stock unfavorited
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Error: Stock not found
    }

}
