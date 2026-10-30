package rahioui.youssef.account_service.service;

import org.springframework.stereotype.Service;

import rahioui.youssef.account_service.repository.AccountRepository;

@Service
public class AccountService {
  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
}
