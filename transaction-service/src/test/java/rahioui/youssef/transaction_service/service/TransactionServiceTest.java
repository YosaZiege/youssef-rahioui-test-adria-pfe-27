package rahioui.youssef.transaction_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rahioui.youssef.transaction_service.model.Transaction;
import rahioui.youssef.transaction_service.model.TransactionType;
import rahioui.youssef.transaction_service.repository.TransactionRepository;

class TransactionServiceTest {
  private TransactionRepository repository;
  private AccountOperations accounts;
  private TransactionService service;
  private UUID source;
  private UUID destination;

  @BeforeEach
  void setUp() {
    repository = mock(TransactionRepository.class);
    accounts = mock(AccountOperations.class);
    service = new TransactionService(repository, accounts, new BigDecimal("1000"));
    source = UUID.randomUUID();
    destination = UUID.randomUUID();
    when(accounts.getAccount(source))
        .thenReturn(new AccountSnapshot(source, "ACTIVE", "EUR", new BigDecimal("500")));
    when(accounts.getAccount(destination))
        .thenReturn(new AccountSnapshot(destination, "ACTIVE", "EUR", BigDecimal.ZERO));
    when(repository.saveAndFlush(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(repository.saveAllAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  void createsDebitAfterCheckingAccount() {
    Transaction result = service.createTransaction(source, new BigDecimal("25"), "EUR", "FOOD", "FR");
    assertEquals(TransactionType.DEBIT, result.getType());
    assertEquals(source, result.getAccountId());
    assertNotNull(result.getCreatedAt());
    verify(accounts).getAccount(source);
    verify(accounts).debit(source, new BigDecimal("25"), "EUR");
    verify(repository).saveAndFlush(any(Transaction.class));
  }

  @Test
  void rejectsOverLimitAndInsufficientBalanceWithoutDebit() {
    assertThrows(IllegalArgumentException.class,
        () -> service.createTransaction(source, new BigDecimal("1001"), "EUR", null, null));
    assertThrows(IllegalArgumentException.class,
        () -> service.createTransaction(source, new BigDecimal("501"), "EUR", null, null));
    verify(accounts, never()).debit(any(), any(), any());
  }

  @Test
  void transferCreatesLinkedDebitAndCreditEntries() {
    List<Transaction> entries = service.transfer(source, destination, new BigDecimal("50"), "EUR");
    assertEquals(2, entries.size());
    assertEquals(TransactionType.DEBIT, entries.get(0).getType());
    assertEquals(TransactionType.CREDIT, entries.get(1).getType());
    assertNotNull(entries.get(0).getTransferId());
    assertEquals(entries.get(0).getTransferId(), entries.get(1).getTransferId());
    verify(accounts).debit(source, new BigDecimal("50"), "EUR");
    verify(accounts).credit(destination, new BigDecimal("50"), "EUR");
  }

  @Test
  void refundsSourceWhenDestinationCreditFails() {
    doThrow(new IllegalStateException("credit failed"))
        .when(accounts).credit(destination, new BigDecimal("50"), "EUR");
    assertThrows(IllegalStateException.class,
        () -> service.transfer(source, destination, new BigDecimal("50"), "EUR"));
    verify(accounts).credit(source, new BigDecimal("50"), "EUR");
    verify(repository, never()).saveAllAndFlush(any());
  }

  @Test
  void refundsDebitWhenTransactionPersistenceFails() {
    when(repository.saveAndFlush(any(Transaction.class))).thenThrow(new IllegalStateException("db failed"));
    assertThrows(IllegalStateException.class,
        () -> service.createTransaction(source, BigDecimal.ONE, "EUR", "FOOD", "FR"));
    verify(accounts).credit(source, BigDecimal.ONE, "EUR");
  }
}
