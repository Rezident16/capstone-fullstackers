package stocks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import stocks.data.LikeRepository;
import stocks.data.MessageRepository;
import stocks.data.UserRepository;
import stocks.domain.LikeService;
import stocks.domain.Result;
import stocks.domain.ResultType;
import stocks.models.AppUser;
import stocks.models.Like;
import stocks.models.Message;
import stocks.security.JwtConverter;
import org.springframework.security.core.userdetails.User;

import java.sql.Timestamp;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LikeControllerTest {
    @MockBean
    LikeRepository likeRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    MessageRepository messageRepository;

    @MockBean
    LikeService likeService;

    @Autowired
    MockMvc mvc;

    @Autowired
    JwtConverter jwtConverter;

    String token;

    /*
    @BeforeEach
    void setup(){
        AppUser appUser = new AppUser("johndoe@example.com", "John", "Doe", "password@2024", 1, 1, "johndoe");
        when(userRepository.findByUsername("johndoe")).thenReturn(appUser);
        User user = new User(
                appUser.getUsername(),
                appUser.getPassword(),
                List.of(new SimpleGrantedAuthority("USER"))
        );
        token = jwtConverter.getTokenFromUser(user);
    }
    */


    @Test
    void addShouldReturn400WhenEmpty() throws Exception{
        var request = post("/api/message/like").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer" + token);

        mvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        Like like= new Like();
        String likeJson = jsonMapper.writeValueAsString(like);

        var request = post("/api/message/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(likeJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    void addShouldReturn415WhenMultipart() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        Like like = new Like(0, true, 2, 2);
        String likeJson = jsonMapper.writeValueAsString(like);

        var request = post("/api/message/like")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)
                .content(likeJson);

        mvc.perform(request)
                .andExpect(status().isUnsupportedMediaType());
    }

    /*
    @Test
    void addShouldReturn201() throws Exception {
        AppUser appUser = new AppUser("janedoe@example.com", "Jane", "Doe", "p@ssword2024", 2, 2, "janedoe");
        when(userRepository.findById(2)).thenReturn(appUser);

        Message message = new Message(1, 2, 1, "Some Message", new Timestamp(System.currentTimeMillis()));
        when(messageRepository.findById(1)).thenReturn(message);

        Like like = new Like(0, true, 2, 1);
        Like expected = new Like(1, true, 2, 1);
        when(likeRepository.add(any(Like.class))).thenReturn(expected);

        ObjectMapper jsonMapper = new ObjectMapper();
        String likeJson = jsonMapper.writeValueAsString(like);
        String expectedJson = jsonMapper.writeValueAsString(expected);

        var request = post("/api/message/like")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(likeJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }
    */



    // Update
    @Test
    void updateShouldReturn204WhenSuccessful() throws Exception {

        Like updatedLike = new Like(1, true, 2, 1);
        Result<Like> result = new Result<>();
        result.setPayload(updatedLike);

        when(likeService.update(any(Like.class))).thenReturn(result);

        var request = put("/api/message/like/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedLike));

        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void updateShouldReturn404WhenLikeNotFound() throws Exception {
        Like updatedLike = new Like(999, true, 2, 1);
        Result<Like> result = new Result<>();
        result.addMessage("Like not found", ResultType.NOT_FOUND);

        when(likeService.update(any(Like.class))).thenReturn(result);

        var request = put("/api/message/like/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedLike));

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void updateShouldReturn400WhenIdMismatch() throws Exception {
        Like updatedLike = new Like(2, true, 2, 1);

        var request = put("/api/message/like/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedLike));

        mvc.perform(request)
                .andExpect(status().isConflict());
    }

    // delete
    @Test
    void deleteShouldReturn204WhenSuccessful() throws Exception {
        Result<Void> result = new Result<>();
        result.setPayload(null);

        Like like = new Like(0, true, 2, 1);
        Like expected = new Like(2, true, 2, 1);

        when(likeRepository.add(any(Like.class))).thenReturn(expected);

        when(likeService.delete(2)).thenReturn(result.isSuccess());

        mvc.perform(delete("/api/message/like/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404WhenNotFound() throws Exception {
        Result<Void> result = new Result<>();
        result.addMessage("Like not found", ResultType.NOT_FOUND);

        when(likeService.delete(999)).thenReturn(result.isSuccess());

        mvc.perform(delete("/api/message/like/999"))
                .andExpect(status().isNotFound());
    }

}