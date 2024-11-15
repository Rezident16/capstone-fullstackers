package stocks.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stocks.data.StockRepository;
import stocks.models.Stock;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/stocks")
public class StockController {

    private final StockRepository repository;

    public StockController(StockRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Stock>> seeAll() {
        List<Stock> stocks = repository.seeAll();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<Stock> seeOne(@PathVariable int stockId) {
        Stock stock = repository.seeOne(stockId);
        if (stock == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(stock);
    }

    @PostMapping
    public ResponseEntity<Stock> add(@RequestBody Stock stock) {
        Stock result = repository.add(stock);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{stockId}")
    public ResponseEntity<Void> update(@PathVariable int stockId, @RequestBody Stock stock) {
        if (stockId != stock.getStockId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        boolean success = repository.update(stock);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> delete(@PathVariable int stockId) {
        if (repository.delete(stockId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
