package rahioui.youssef.account_service.web.rest;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import rahioui.youssef.account_service.service.AccountConflictException;
import rahioui.youssef.account_service.service.AccountNotFoundException;

@RestControllerAdvice
public class AccountExceptionHandler {
  @ExceptionHandler(AccountNotFoundException.class)
  public ProblemDetail notFound(AccountNotFoundException exception) {
    return problem(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler({
      AccountConflictException.class,
      OptimisticLockingFailureException.class
  })
  public ProblemDetail conflict(Exception exception) {
    return problem(HttpStatus.CONFLICT, exception.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail badRequest(IllegalArgumentException exception) {
    return problem(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  private ProblemDetail problem(HttpStatus status, String message) {
    ProblemDetail detail = ProblemDetail.forStatus(status);
    detail.setDetail(message);
    return detail;
  }
}
