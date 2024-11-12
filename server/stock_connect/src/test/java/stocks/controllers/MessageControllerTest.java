package stocks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import stocks.domain.MessageService;
import stocks.models.Message;
import stocks.domain.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

    @MockBean
    MessageService service;

    @Autowired
    MockMvc mvc;

    @Test
    void addShouldReturn400WhenEmpty() throws Exception {
        var request = post("/api/message")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();

        Message message = new Message();
        String messageJson = jsonMapper.writeValueAsString(message);

        var request = post("/api/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageJson);

        mvc.perform(request)
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findByIdShouldReturn404WhenNotFound() throws Exception {
        when(service.findById(1)).thenReturn(null);

        mvc.perform(get("/api/message/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdShouldReturn200WhenFound() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Message message = new Message(1, 1, 1, "This is a test message", timestamp);

        when(service.findById(1)).thenReturn(message);

        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(message);

        mvc.perform(get("/api/message/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void updateShouldReturn400WhenIdMismatch() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Message message = new Message(1, 1, 1, "Updated message", timestamp);

        var request = put("/api/message/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(message));

        mvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    void deleteShouldReturn404WhenNotFound() throws Exception {
        when(service.deleteById(1)).thenReturn(false);

        mvc.perform(delete("/api/message/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturn204WhenSuccessful() throws Exception {
        when(service.deleteById(1)).thenReturn(true);

        mvc.perform(delete("/api/message/1"))
                .andExpect(status().isNoContent());
    }
}
