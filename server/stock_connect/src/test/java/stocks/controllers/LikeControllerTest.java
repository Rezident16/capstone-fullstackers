package stocks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import stocks.data.LikeRepository;
import stocks.data.MessageRepository;
import stocks.data.UserRepository;
import stocks.models.AppUser;
import stocks.models.Like;
import stocks.models.Message;
import stocks.security.JwtConverter;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LikeControllerTest {
    @MockBean
    LikeRepository repository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    MessageRepository messageRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    JwtConverter jwtConverter;

    String token;

//    @BeforeEach
//    void setup(){
//        AppUser appUser = new AppUser("johndoe@example.com", "John", "Doe", "password@2024", 1, 1, "johndoe");
//        when(userRepository.findByUsername("johndoe")).thenReturn(appUser);
//        token = jwtConverter.getTokenFromUser(appUser);
//    }

    @Test
    void addShouldReturn400WhenEmpty() throws Exception{
        var request = post("/api/message").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer" + token);

        mvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        Like like= new Like();
        String likeJson = jsonMapper.writeValueAsString(like);

        var request = post("/api/message")
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

        var request = post("/api/message")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)
                .content(likeJson);

        mvc.perform(request)
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void addShouldReturn201() throws Exception {

        AppUser appUser = new AppUser("janedoe@example.com", "Jane", "Doe", "p@ssword2024", 2, 2, "janedoe");
        when(userRepository.findById(2)).thenReturn(appUser);

        Message message = new Message(1, 2, 1, "Some Message", new Timestamp(System.currentTimeMillis()));
        when(messageRepository.findById(1)).thenReturn(message);

        Like like = new Like(0, true, 2, 1);
        Like expected = new Like(1, true, 2, 1);
        when(repository.add(any(Like.class))).thenReturn(expected);

        ObjectMapper jsonMapper = new ObjectMapper();
        String likeJson = jsonMapper.writeValueAsString(like);
        String expectedJson = jsonMapper.writeValueAsString(expected);

        var request = post("/api/message")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(likeJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }

}