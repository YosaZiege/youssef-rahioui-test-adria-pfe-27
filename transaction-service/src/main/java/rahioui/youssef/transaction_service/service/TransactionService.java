package rahioui.youssef.transaction_service.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rahioui.youssef.transaction_service.model.Transaction;
import rahioui.youssef.transaction_service.model.TransactionType;
import rahioui.youssef.transaction_service.repository.TransactionRepository;

@Service
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final AccountOperations accounts;
  private final BigDecimal maxAmount;

  public TransactionService(TransactionRepository transactionRepository, AccountOperations accounts,
      @Value("${bank.transaction.max-amount}") BigDecimal maxAmount) {
    this.transactionRepository = transactionRepository;
    this.accounts = accounts;
    this.maxAmount = maxAmount;
  }

  @Transactional
  public Transaction createTransaction(UUID accountId, BigDecimal amount, String currency,
      String merchantCategory, String country) {
    validateAmount(amount);
    validateAccount(accounts.getAccount(accountId), currency, amount);
    accounts.debit(accountId, amount, currency);
    try {
      return transactionRepository.saveAndFlush(
          entry(accountId, amount, currency, merchantCategory, country, TransactionType.DEBIT, null));
    } catch (RuntimeException failure) {
      compensate(failure, () -> accounts.credit(accountId, amount, currency));
      throw failure;
    }
  }

  @Transactional(readOnly = true)
  public Transaction getTransaction(UUID transactionId) {
    return transactionRepository.findById(transactionId)
        .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + transactionId));
  }

  @Transactional(readOnly = true)
  public List<Transaction> listTransactions(UUID accountId) {
    return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
  }

  @Transactional
  public List<Transaction> transfer(UUID sourceAccountId, UUID destinationAccountId,
      BigDecimal amount, String currency) {
    validateAmount(amount);
    if (sourceAccountId == null || destinationAccountId == null || sourceAccountId.equals(destinationAccountId)) {
      throw new IllegalArgumentException("Transfer requires two different accounts");
    }
    validateAccount(accounts.getAccount(sourceAccountId), currency, amount);
    validateAccount(accounts.getAccount(destinationAccountId), currency, null);

    accounts.debit(sourceAccountId, amount, currency);
    try {
      accounts.credit(destinationAccountId, amount, currency);
    } catch (RuntimeException failure) {
      compensate(failure, () -> accounts.credit(sourceAccountId, amount, currency));
      throw failure;
    }

    UUID transferId = UUID.randomUUID();
    try {
      return transactionRepository.saveAllAndFlush(List.of(
          entry(sourceAccountId, amount, currency, null, null, TransactionType.DEBIT, transferId),
          entry(destinationAccountId, amount, currency, null, null, TransactionType.CREDIT, transferId)));
    } catch (RuntimeException failure) {
      compensate(failure, () -> accounts.debit(destinationAccountId, amount, currency));
      compensate(failure, () -> accounts.credit(sourceAccountId, amount, currency));
      throw failure;
    }
  }

  private Transaction entry(UUID accountId, BigDecimal amount, String currency,
      String merchantCategory, String country, TransactionType type, UUID transferId) {
    Transaction transaction = new Transaction();
    transaction.setAccountId(accountId);
    transaction.setAmount(amount);
    transaction.setCurrency(currency);
    transaction.setMerchantCategory(merchantCategory);
    transaction.setCountry(country);
    transaction.setCreatedAt(Instant.now());
    transaction.setType(type);
    transaction.setTransferId(transferId);
    return transaction;
  }

  private void validateAmount(BigDecimal amount) {
    if (amount == null || amount.signum() <= 0 || amount.compareTo(maxAmount) > 0) {
      throw new IllegalArgumentException("Amount must be positive and at most " + maxAmount);
    }
  }

  private void validateAccount(AccountSnapshot account, String currency, BigDecimal debitAmount) {
    if (account == null || !"ACTIVE".equals(account.status())) {
      throw new IllegalArgumentException("Account does not exist or is not active");
    }
    if (currency == null || !currency.equals(account.currency())) {
      throw new IllegalArgumentException("Currency does not match account");
    }
    if (debitAmount != null && (account.balance() == null || account.balance().compareTo(debitAmount) < 0)) {
      throw new IllegalArgumentException("Insufficient funds");
    }
  }

  private void compensate(RuntimeException failure, Runnable action) {
    try {
      action.run();
    } catch (RuntimeException compensationFailure) {
      failure.addSuppressed(compensationFailure);
    }
  }
}
