package rahioui.youssef.chatbot_service.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfiguration {
  @Bean
  ChatClient bankChatClient(ChatClient.Builder builder, ToolCallbackProvider mcpTools) {
    return builder
        .defaultSystem("You are a banking assistant. Use the available banking tools to look up account and transaction data. Never invent balances or claim that a banking operation succeeded unless a tool confirms it.")
        .defaultToolCallbacks(mcpTools)
        .build();
  }
}
