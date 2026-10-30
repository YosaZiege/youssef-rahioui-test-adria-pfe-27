package rahioui.youssef.account_service.mcp;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import rahioui.youssef.account_service.model.Account;
import rahioui.youssef.account_service.service.AccountService;

@Component
public class AccountTools {
  private final AccountService accountService;

  public AccountTools(AccountService accountService) {
    this.accountService = accountService;
  }

  @McpTool(name = "get_account", description = "Look up a bank account by UUID and return its status, currency and balance")
  public AccountDetails getAccount(
      @McpToolParam(description = "Bank account UUID", required = true) String accountId) {
    Account account = accountService.getAccount(UUID.fromString(accountId));
    return new AccountDetails(account.getId(), account.getCustomerReference(),
        account.getStatus().name(), account.getCurrency(), account.getBalance());
  }

  @McpTool(name = "get_account_balance", description = "Get the current balance of a bank account by UUID")
  public BigDecimal getBalance(
      @McpToolParam(description = "Bank account UUID", required = true) String accountId) {
    return accountService.getBalance(UUID.fromString(accountId));
  }

  public record AccountDetails(UUID id, String customerReference, String status,
      String currency, BigDecimal balance) {
  }
}
