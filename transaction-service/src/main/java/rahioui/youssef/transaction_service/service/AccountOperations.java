package rahioui.youssef.transaction_service.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountOperations {
  AccountSnapshot getAccount(UUID accountId);

  void debit(UUID accountId, BigDecimal amount, String currency);

  void credit(UUID accountId, BigDecimal amount, String currency);
}
