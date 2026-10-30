package rahioui.youssef.account_service.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rahioui.youssef.account_service.model.Account;
import rahioui.youssef.account_service.model.AccountStatus;
import rahioui.youssef.account_service.repository.AccountRepository;

@Service
public class AccountService {
  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Transactional
  public Account createAccount(String customerReference, String currency, BigDecimal openingBalance) {
    if (customerReference == null || customerReference.isBlank() || currency == null || currency.isBlank()) {
      throw new IllegalArgumentException("Customer reference and currency are required");
    }
    if (openingBalance == null || openingBalance.signum() < 0) {
      throw new IllegalArgumentException("Opening balance must be non-negative");
    }
    Account account = new Account();
    account.setCustomerReference(customerReference);
    account.setCurrency(currency);
    account.setStatus(AccountStatus.ACTIVE);
    account.setBalance(openingBalance);
    return accountRepository.save(account);
  }

  @Transactional(readOnly = true)
  public Account getAccount(UUID accountId) {
    return accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException(accountId));
  }

  @Transactional(readOnly = true)
  public BigDecimal getBalance(UUID accountId) {
    return getAccount(accountId).getBalance();
  }

  @Transactional
  public Account debit(UUID accountId, BigDecimal amount, String currency) {
    validateAmount(amount);
    Account account = getAccount(accountId);
    validateAccount(account, currency);
    if (account.getBalance().compareTo(amount) < 0) {
      throw new IllegalArgumentException("Insufficient funds");
    }
    account.setBalance(account.getBalance().subtract(amount));
    return saveBalance(account);
  }

  @Transactional
  public Account credit(UUID accountId, BigDecimal amount, String currency) {
    validateAmount(amount);
    Account account = getAccount(accountId);
    validateAccount(account, currency);
    account.setBalance(account.getBalance().add(amount));
    return saveBalance(account);
  }

  private Account saveBalance(Account account) {
    try {
      return accountRepository.saveAndFlush(account);
    } catch (OptimisticLockingFailureException exception) {
      throw new AccountConflictException("Account balance changed; retry the operation", exception);
    }
  }

  private void validateAmount(BigDecimal amount) {
    if (amount == null || amount.signum() <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }
  }

  private void validateAccount(Account account, String currency) {
    if (account.getStatus() != AccountStatus.ACTIVE) {
      throw new IllegalArgumentException("Account is not active");
    }
    if (currency == null || !account.getCurrency().equals(currency)) {
      throw new IllegalArgumentException("Currency does not match account");
    }
  }
}
