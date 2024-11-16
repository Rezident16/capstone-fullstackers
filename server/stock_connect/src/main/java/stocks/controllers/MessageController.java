package stocks.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import stocks.domain.MessageService;
import stocks.models.AppUser;
import stocks.models.Message;
import stocks.domain.Result;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<Message> findById(@PathVariable int messageId) {
        Message message = service.findById(messageId);
        if (message == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/stock/{stockId}")
    public ResponseEntity<List<Message>> findByStockId(@PathVariable int stockId) {
        System.out.println("stockId: " + stockId);
        List<Message> messages = service.findByStockId(stockId);
        System.out.println(messages.size() + " in controller");
        if (messages == null || messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Message message) {
        // Check if userId is provided in the request body
        if (message.getUserId() <= 0) {
            return new ResponseEntity<>("User ID is required", HttpStatus.BAD_REQUEST);
        }

        // Add the message with the provided user_id
        Result<Message> result = service.add(message);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }


    @PutMapping("/{messageId}")
    public ResponseEntity<Object> update(@PathVariable int messageId, @RequestBody Message message) {
        if (messageId != message.getMessageId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Message> result = service.update(message);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteById(@PathVariable int messageId) {
        if (service.deleteById(messageId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
