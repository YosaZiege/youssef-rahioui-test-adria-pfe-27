package rahioui.youssef.transaction_service.web.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import rahioui.youssef.transaction_service.service.AccountOperations;
import rahioui.youssef.transaction_service.service.AccountSnapshot;

@SpringBootTest(properties = "eureka.client.enabled=false")
@AutoConfigureMockMvc
class TransactionRestControllerTest {
  @Autowired
  MockMvc mvc;

  @MockitoBean
  AccountOperations accounts;

  @Test
  void createsTransactionFromJsonRequest() throws Exception {
    UUID accountId = UUID.randomUUID();
    when(accounts.getAccount(accountId)).thenReturn(
        new AccountSnapshot(accountId, "ACTIVE", "EUR", new BigDecimal("500.00")));

    mvc.perform(post("/transactions")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"accountId\":\"" + accountId
            + "\",\"amount\":25.00,\"currency\":\"EUR\",\"merchantCategory\":\"FOOD\",\"country\":\"FR\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.accountId").value(accountId.toString()))
        .andExpect(jsonPath("$.type").value("DEBIT"))
        .andExpect(jsonPath("$.amount").value(25.00));
  }
}
