package rahioui.youssef.chatbot_service.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rahioui.youssef.chatbot_service.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatRestController {
  private final ChatService chatService;

  public ChatRestController(ChatService chatService) {
    this.chatService = chatService;
  }

  @PostMapping
  public ChatResponse chat(@RequestBody ChatRequest request) {
    return new ChatResponse(chatService.reply(request.message()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail invalidMessage(IllegalArgumentException exception) {
    ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    detail.setDetail(exception.getMessage());
    return detail;
  }

  public record ChatRequest(String message) {
  }

  public record ChatResponse(String answer) {
  }
}
