package stocks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import stocks.data.UserStocksRepository;
import stocks.models.UserStock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserStocksControllerTest {

    @MockBean
    UserStocksRepository repository;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper jsonMapper;

    @Test
    void addShouldReturn201WhenSuccessful() throws Exception {
        UserStock userStock = new UserStock(0, 1, 2);
        UserStock expected = new UserStock(1, 1, 2);

        when(repository.add(any())).thenReturn(expected);

        String userStockJson = jsonMapper.writeValueAsString(userStock);

        mvc.perform(post("/api/user-stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userStockJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userStockId").value(1));
    }

    @Test
    void addShouldReturn500WhenRepositoryFails() throws Exception {
        UserStock userStock = new UserStock(0, 1, 2);

        when(repository.add(any())).thenReturn(null);

        String userStockJson = jsonMapper.writeValueAsString(userStock);

        mvc.perform(post("/api/user-stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userStockJson))
                .andExpect(status().isInternalServerError());
        System.out.println();
    }
    @Test
    void deleteShouldReturn204WhenSuccessful() throws Exception {
        when(repository.delete(1)).thenReturn(true);

        mvc.perform(delete("/api/user-stocks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404WhenNotFound() throws Exception {
        when(repository.delete(1)).thenReturn(false);

        mvc.perform(delete("/api/user-stocks/1"))
                .andExpect(status().isNotFound());
    }
}
