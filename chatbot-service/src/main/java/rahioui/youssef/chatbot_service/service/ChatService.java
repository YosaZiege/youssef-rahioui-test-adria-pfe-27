package rahioui.youssef.chatbot_service.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
  private final ChatClient chatClient;

  public ChatService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  public String reply(String message) {
    if (message == null || message.isBlank()) {
      throw new IllegalArgumentException("Message must not be blank");
    }
    return chatClient.prompt().user(message).call().content();
  }
}
