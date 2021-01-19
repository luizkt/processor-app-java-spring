package br.com.fiap.controller;

import br.com.fiap.entity.ApplicationResponseBody;
import br.com.fiap.entity.Transaction;
import br.com.fiap.service.impl.TransactionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test"})
public class TransactionControllerTest {

    @Mock
    private TransactionServiceImpl transactionService;

    private MvcResult result;

    private MockMvc mockMvc;

    TransactionController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        controller = new TransactionController(transactionService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void givenNewTransaction_whenRegisteringIt_shouldAddTransactionSuccessfully() throws Exception {
        Transaction transaction = new Transaction(111, 111000, "1234", 9.80, "Transaction 1");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        ApplicationResponseBody applicationResponseBody = new ApplicationResponseBody("Added the transaction successfully", transaction);

        ObjectMapper mapper = new ObjectMapper();

        when(transactionService.add(Mockito.any(Transaction.class)))
                .thenReturn(applicationResponseBody);

        result = mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transaction)))
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertNotNull(result);
        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    public void givenRegisteredTransaction_whenDeletingIt_shouldRemoveFromRegistration() throws Exception {
        Transaction transaction = new Transaction();

        transaction.setTransactionId(111);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

        ApplicationResponseBody applicationResponseBody = new ApplicationResponseBody("Deleted the transaction successfully", transaction);

        ObjectMapper mapper = new ObjectMapper();

        when(transactionService.deleteTransactionById(transaction.getTransactionId()))
                .thenReturn(applicationResponseBody);

        result = mockMvc.perform(MockMvcRequestBuilders.delete("/transactions/111")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());
    }
}
