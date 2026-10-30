package rahioui.youssef.account_service.web.rest;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import rahioui.youssef.account_service.model.Account;
import rahioui.youssef.account_service.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountRestController {
  private final AccountService accountService;

  public AccountRestController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping
  public ResponseEntity<AccountResponse> create(@RequestBody CreateAccountRequest request) {
    Account account = accountService.createAccount(request.customerReference, request.currency,
        request.openingBalance());

    return ResponseEntity.created(URI.create("/accounts/" + account.getId())).body(AccountResponse.from(account));
  }

  @GetMapping("/{id}")
  public AccountResponse get(@PathVariable UUID id) {
    return AccountResponse.from(accountService.getAccount(id));
  }

  @GetMapping("/{id}/balance")
  public BalanceResponse balance(@PathVariable UUID id) {
    return new BalanceResponse(accountService.getBalance(id));
  }

  @PostMapping("/{id}/debits")
  public AccountResponse debit(@PathVariable UUID id, @RequestBody MovementRequest request) {
    return AccountResponse.from(
        accountService.debit(id, request.amount(), request.currency()));
  }

  @PostMapping("/{id}/credits")
  public AccountResponse credit(@PathVariable UUID id, @RequestBody MovementRequest request) {
    return AccountResponse.from(
        accountService.credit(id, request.amount(), request.currency()));
  }

  public record CreateAccountRequest(
      String customerReference, String currency, BigDecimal openingBalance) {
  }

  public record MovementRequest(BigDecimal amount, String currency) {
  }

  public record BalanceResponse(BigDecimal balance) {
  }

  public record AccountResponse(
      UUID id, String customerReference, String status,
      String currency, BigDecimal balance) {

    public static AccountResponse from(Account account) {
      return new AccountResponse(
          account.getId(),
          account.getCustomerReference(),
          account.getStatus().name(),
          account.getCurrency(),
          account.getBalance());
    }
  }
}
