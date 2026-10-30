package rahioui.youssef.transaction_service.client;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import rahioui.youssef.transaction_service.service.AccountSnapshot;

@FeignClient(name = "account-service", url = "${bank.account-service.url}")
public interface AccountClient {

  @GetMapping("/accounts/{id}")
  AccountSnapshot getAccount(@PathVariable("id") UUID id);

  @PostMapping("/accounts/{id}/debits")
  void debit(@PathVariable("id") UUID id, @RequestBody MovementRequest request);

  @PostMapping("/accounts/{id}/credits")
  void credit(@PathVariable("id") UUID id, @RequestBody MovementRequest request);

  record MovementRequest(BigDecimal amount, String currency) {
  }
}
