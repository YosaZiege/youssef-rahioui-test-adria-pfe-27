package rahioui.youssef.transaction_service.web.rest;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail badRequest(IllegalArgumentException exception) {
    return problem(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(FeignException.NotFound.class)
  public ProblemDetail accountNotFound(FeignException.NotFound exception) {
    return problem(HttpStatus.NOT_FOUND, "Account not found");
  }

  @ExceptionHandler(FeignException.BadRequest.class)
  public ProblemDetail accountRejected(FeignException.BadRequest exception) {
    return problem(HttpStatus.BAD_REQUEST, "Account operation rejected");
  }

  @ExceptionHandler(FeignException.Conflict.class)
  public ProblemDetail accountConflict(FeignException.Conflict exception) {
    return problem(HttpStatus.CONFLICT, "Account balance conflict");
  }

  @ExceptionHandler(FeignException.class)
  public ProblemDetail accountUnavailable(FeignException exception) {
    return problem(HttpStatus.SERVICE_UNAVAILABLE, "Account service unavailable");
  }

  private ProblemDetail problem(HttpStatus status, String message) {
    ProblemDetail detail = ProblemDetail.forStatus(status);
    detail.setDetail(message);
    return detail;
  }
}
