package org.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.dto.SaleRequest;
import org.backend.dto.SaleReturnRequest;
import org.backend.model.Sale;
import org.backend.service.SaleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaleController.class)
@AutoConfigureMockMvc(addFilters = false)
class SaleControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SaleService saleService;

    @Autowired
    ObjectMapper om;

    private Sale sampleSale(Long id) {
        Sale s = new Sale();
        s.setId(id);
        s.setDate(LocalDateTime.now());
        s.setClientId(1L);
        s.setClientName("Cliente Teste");
        s.setOrigem("sistema");
        s.setPaymentMethod("PIX");
        s.setPaymentStatus("PAID");
        s.setTotal(BigDecimal.TEN);
        s.setObservacoes("ok");
        return s;
    }

    @Test
    void list_sales_ok() throws Exception {
        Mockito.when(saleService.list()).thenReturn(List.of(sampleSale(1L), sampleSale(2L)));
        mvc.perform(get("/api/sales"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(2)))
           .andExpect(jsonPath("$[0].id", is(1)))
           .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void create_sale_ok() throws Exception {
        SaleRequest req = new SaleRequest();
        req.setClientId(1L);
        req.setClientName("Cliente Teste");
        Sale created = sampleSale(10L);
        Mockito.when(saleService.createFromRequest(any(SaleRequest.class))).thenReturn(created);

        mvc.perform(post("/api/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(10)))
           .andExpect(jsonPath("$.clientName", is("Cliente Teste")));
    }

    @Test
    void register_return_ok() throws Exception {
        Sale updated = sampleSale(5L);
        SaleReturnRequest r = new SaleReturnRequest();
        r.setItemIndex(0);
        r.setQtd(1);
        r.setMotivo("Defeito");
        Mockito.when(saleService.registerReturn(eq(5L), any(SaleReturnRequest.class))).thenReturn(updated);

        mvc.perform(post("/api/sales/5/returns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(r)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(5)));
    }
}
