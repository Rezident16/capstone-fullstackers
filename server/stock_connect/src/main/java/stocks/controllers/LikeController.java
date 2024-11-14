package stocks.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stocks.domain.LikeService;
import stocks.domain.Result;
import stocks.models.Like;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/message/like")
public class LikeController {
    private final LikeService service;
    public LikeController(LikeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Like> findAll(){
        return service.findAll();
    }

    @GetMapping("/id/{likeId}")
    public ResponseEntity<Like> findById(@PathVariable int likeId){
        Like like = service.findById(likeId);
        if(like == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(like);
    }

    @GetMapping("/user/{userId}")
    public List<Like> findByUserId(@PathVariable int userId){
        List<Like> likes = service.findByUserId(userId);
        return likes;
    }


    @GetMapping("/{messageId}")
    public List<Like> findByMessageId(@PathVariable int messageId){
        List<Like> likes = service.findByMessageId(messageId);
        return likes;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Like like) {
        like.setLiked(like.isLiked());

        Result<Like> result = service.add(like);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }


    @PutMapping("/id/{likeId}")
    public ResponseEntity<Object> update(@PathVariable int likeId, @RequestBody Like like){
        if(likeId != like.getLikeId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result<Like> result = service.update(like);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/id/{likeId}")
    public ResponseEntity<Void> delete(@PathVariable int likeId){
        if(service.delete(likeId)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}