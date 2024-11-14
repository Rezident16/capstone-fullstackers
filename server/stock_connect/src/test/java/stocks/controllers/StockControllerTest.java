package stocks.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import stocks.data.StockRepository;
import stocks.models.Stock;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StockControllerTest {

    @MockBean
    StockRepository repository;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper jsonMapper;

    @Test
    void seeAllShouldReturn200() throws Exception {
        List<Stock> stocks = Arrays.asList(
                new Stock(1, "Apple Inc.", "Leading technology company", "AAPL"),
                new Stock(2, "Tesla Inc.", "Electric vehicle manufacturer", "TSLA")
        );

        when(repository.seeAll()).thenReturn(stocks);

        mvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].stockName").value("Apple Inc."))
                .andExpect(jsonPath("$[1].stockName").value("Tesla Inc."));
    }

    @Test
    void seeOneShouldReturn200WhenFound() throws Exception {
        Stock stock = new Stock(1, "Apple Inc.", "Leading technology company", "AAPL");

        when(repository.seeOne(1)).thenReturn(stock);

        mvc.perform(get("/api/stocks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stockName").value("Apple Inc."))
                .andExpect(jsonPath("$.ticker").value("AAPL"));
    }

    @Test
    void seeOneShouldReturn404WhenNotFound() throws Exception {
        when(repository.seeOne(100)).thenReturn(null);

        mvc.perform(get("/api/stocks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addShouldReturn201() throws Exception {
        Stock stock = new Stock(0, "Microsoft Corp.", "Software giant", "MSFT");
        Stock expected = new Stock(1, "Microsoft Corp.", "Software giant", "MSFT");

        when(repository.add(any())).thenReturn(expected);

        String stockJson = jsonMapper.writeValueAsString(stock);

        mvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stockJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stockId").value(1))
                .andExpect(jsonPath("$.stockName").value("Microsoft Corp."));
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {
        Stock stock = new Stock(); // Empty stock
        String stockJson = jsonMapper.writeValueAsString(stock);

        mvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stockJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateShouldReturn204WhenSuccessful() throws Exception {
        Stock stock = new Stock(1, "Alphabet Inc.", "Search engine giant", "GOOGL");

        when(repository.update(any())).thenReturn(true);

        String stockJson = jsonMapper.writeValueAsString(stock);

        mvc.perform(put("/api/stocks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stockJson))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateShouldReturn409WhenIdMismatch() throws Exception {
        Stock stock = new Stock(2, "Meta Platforms", "Social media giant", "META");

        String stockJson = jsonMapper.writeValueAsString(stock);

        mvc.perform(put("/api/stocks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stockJson))
                .andExpect(status().isConflict());
    }

    @Test
    void updateShouldReturn404WhenNotFound() throws Exception {
        Stock stock = new Stock(1, "Netflix Inc.", "Streaming platform", "NFLX");

        when(repository.update(any())).thenReturn(false);

        String stockJson = jsonMapper.writeValueAsString(stock);

        mvc.perform(put("/api/stocks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stockJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturn204WhenSuccessful() throws Exception {
        when(repository.delete(1)).thenReturn(true);

        mvc.perform(delete("/api/stocks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404WhenNotFound() throws Exception {
        when(repository.delete(100)).thenReturn(false);

        mvc.perform(delete("/api/stocks/100"))
                .andExpect(status().isNotFound());
    }
}
