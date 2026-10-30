package rahioui.youssef.account_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import rahioui.youssef.account_service.model.Account;
import rahioui.youssef.account_service.model.AccountStatus;
import rahioui.youssef.account_service.repository.AccountRepository;

class AccountServiceTest {
  private AccountRepository repository;
  private AccountService service;
  private UUID id;
  private Account account;

  @BeforeEach
  void setUp() {
    repository = mock(AccountRepository.class);
    service = new AccountService(repository);
    id = UUID.randomUUID();
    account = new Account();
    account.setId(id);
    account.setStatus(AccountStatus.ACTIVE);
    account.setCurrency("EUR");
    account.setBalance(new BigDecimal("100.00"));
    when(repository.findById(id)).thenReturn(Optional.of(account));
    when(repository.saveAndFlush(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  void createsActiveAccountAndReadsBalance() {
    when(repository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
    Account created = service.createAccount("customer-1", "EUR", new BigDecimal("20.00"));
    assertEquals(AccountStatus.ACTIVE, created.getStatus());
    assertEquals(new BigDecimal("20.00"), created.getBalance());
    assertEquals(new BigDecimal("100.00"), service.getBalance(id));
  }

  @Test
  void debitsAndCreditsBalance() {
    service.debit(id, new BigDecimal("25.00"), "EUR");
    assertEquals(new BigDecimal("75.00"), account.getBalance());
    service.credit(id, new BigDecimal("10.00"), "EUR");
    assertEquals(new BigDecimal("85.00"), account.getBalance());
  }

  @Test
  void rejectsInvalidMovements() {
    assertThrows(IllegalArgumentException.class, () -> service.debit(id, BigDecimal.ZERO, "EUR"));
    assertThrows(IllegalArgumentException.class, () -> service.debit(id, new BigDecimal("101"), "EUR"));
    assertThrows(IllegalArgumentException.class, () -> service.credit(id, BigDecimal.ONE, "USD"));
    account.setStatus(AccountStatus.BLOCKED);
    assertThrows(IllegalArgumentException.class, () -> service.debit(id, BigDecimal.ONE, "EUR"));
    verify(repository, never()).saveAndFlush(any(Account.class));
  }

  @Test
  void reportsMissingAccountAndConcurrentChange() {
    assertThrows(AccountNotFoundException.class, () -> service.getAccount(UUID.randomUUID()));
    when(repository.saveAndFlush(any(Account.class)))
        .thenThrow(new ObjectOptimisticLockingFailureException(Account.class, id));
    assertThrows(AccountConflictException.class, () -> service.debit(id, BigDecimal.ONE, "EUR"));
  }
}
