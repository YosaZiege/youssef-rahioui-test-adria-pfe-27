package rahioui.youssef.transaction_service.web.rest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import rahioui.youssef.transaction_service.model.Transaction;
import rahioui.youssef.transaction_service.service.TransactionService;

@RestController
@RequestMapping("/transactions")

public class TransactionRestController {
  private final TransactionService transactionService;

  public TransactionRestController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping
  public ResponseEntity<Transaction> create(@RequestBody CreateTransactionRequest request) {
    Transaction transaction = transactionService.createTransaction(
        request.accountId(),
        request.amount(),
        request.currency(),
        request.merchantCategory(),
        request.country());

    return ResponseEntity
        .created(URI.create("/transactions/" + transaction.getId()))
        .body(transaction);
  }

  @GetMapping("/{id}")
  public Transaction get(@PathVariable UUID id) {
    return transactionService.getTransaction(id);
  }

  @GetMapping("/account/{accountId}")
  public List<Transaction> listForAccount(@PathVariable UUID accountId) {
    return transactionService.listTransactions(accountId);
  }

  @PostMapping("/transfers")
  public ResponseEntity<List<Transaction>> transfer(@RequestBody TransferRequest request) {
    List<Transaction> entries = transactionService.transfer(
        request.sourceAccountId(),
        request.destinationAccountId(),
        request.amount(),
        request.currency());

    return ResponseEntity.status(HttpStatus.CREATED).body(entries);
  }

  public record CreateTransactionRequest(
      UUID accountId, BigDecimal amount, String currency,
      String merchantCategory, String country) {
  }

  public record TransferRequest(
      UUID sourceAccountId, UUID destinationAccountId,
      BigDecimal amount, String currency) {
  }
}
