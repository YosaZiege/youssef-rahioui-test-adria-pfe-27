package rahioui.youssef.transaction_service.client;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import rahioui.youssef.transaction_service.service.AccountOperations;
import rahioui.youssef.transaction_service.service.AccountSnapshot;

@Component
public class FeignAccountOperations implements AccountOperations {
  private final AccountClient client;

  public FeignAccountOperations(AccountClient client) {
    this.client = client;
  }

  @Override
  public AccountSnapshot getAccount(UUID accountId) {
    return client.getAccount(accountId);
  }

  @Override
  public void debit(UUID accountId, BigDecimal amount, String currency) {
    client.debit(accountId, new AccountClient.MovementRequest(amount, currency));
  }

  @Override
  public void credit(UUID accountId, BigDecimal amount, String currency) {
    client.credit(accountId, new AccountClient.MovementRequest(amount, currency));
  }
}
