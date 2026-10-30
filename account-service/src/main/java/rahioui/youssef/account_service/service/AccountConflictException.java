package rahioui.youssef.account_service.service;

public class AccountConflictException extends RuntimeException {
  public AccountConflictException(String message, Throwable cause) {
    super(message, cause);
  }
}
