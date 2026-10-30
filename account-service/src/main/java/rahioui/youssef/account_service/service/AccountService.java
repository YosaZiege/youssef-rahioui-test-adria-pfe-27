package rahioui.youssef.account_service.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import rahioui.youssef.account_service.model.Account;
import rahioui.youssef.account_service.repository.AccountRepository;

@Service
public class AccountService {
  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account createAccount(String customerReference, String currency, BigDecimal openingBalance);

  public Account getAccount(UUID accountId);

  public Account debit(UUID accountId, BigDecimal amount, String currency);

  public Account credit(UUID accountId, BigDecimal amount, String currency);
}
