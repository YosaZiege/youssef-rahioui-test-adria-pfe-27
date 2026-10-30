package rahioui.youssef.transaction_service.mcp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import rahioui.youssef.transaction_service.model.Transaction;
import rahioui.youssef.transaction_service.service.TransactionService;

@Component
public class TransactionTools {
  private final TransactionService transactionService;

  public TransactionTools(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @McpTool(name = "get_transaction", description = "Look up a recorded bank transaction by UUID")
  public TransactionDetails getTransaction(
      @McpToolParam(description = "Transaction UUID", required = true) String transactionId) {
    return details(transactionService.getTransaction(UUID.fromString(transactionId)));
  }

  @McpTool(name = "create_transaction", description = "Debit an active bank account and record a transaction; amount must be positive and within the configured limit")
  public TransactionDetails createTransaction(
      @McpToolParam(description = "Account UUID to debit", required = true) String accountId,
      @McpToolParam(description = "Positive transaction amount", required = true) BigDecimal amount,
      @McpToolParam(description = "Currency, e.g. EUR", required = true) String currency,
      @McpToolParam(description = "Merchant category", required = true) String merchantCategory,
      @McpToolParam(description = "Merchant country", required = true) String country) {
    return details(transactionService.createTransaction(
        UUID.fromString(accountId), amount, currency, merchantCategory, country));
  }

  private TransactionDetails details(Transaction transaction) {
    return new TransactionDetails(transaction.getId(), transaction.getAccountId(),
        transaction.getAmount(), transaction.getCurrency(), transaction.getType().name(),
        transaction.getMerchantCategory(), transaction.getCountry(), transaction.getCreatedAt());
  }

  public record TransactionDetails(UUID id, UUID accountId, BigDecimal amount,
      String currency, String type, String merchantCategory, String country, Instant createdAt) {
  }
}
