package rahioui.youssef.account_service.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "eureka.client.enabled=false")
@AutoConfigureMockMvc
class AccountRestControllerTest {
  @Autowired
  MockMvc mvc;

  @Test
  void createsAccountThenDebitsItUsingJsonRequests() throws Exception {
    String response = mvc.perform(post("/accounts")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"customerReference\":\"customer-1\",\"currency\":\"EUR\",\"openingBalance\":500.00}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("ACTIVE"))
        .andReturn().getResponse().getContentAsString();

    String id = JsonPath.read(response, "$.id");
    mvc.perform(post("/accounts/{id}/debits", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"amount\":25.00,\"currency\":\"EUR\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.balance").value(475.00));

    mvc.perform(get("/accounts/{id}/balance", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.balance").value(475.00));
  }
}
