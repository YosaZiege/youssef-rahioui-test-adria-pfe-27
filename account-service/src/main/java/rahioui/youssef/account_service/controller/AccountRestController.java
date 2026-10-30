package rahioui.youssef.account_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rahioui.youssef.account_service.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountRestController {
  private final AccountService accountService;

  public AccountRestController(AccountService accountService) {
    this.accountService = accountService;
  }
}
