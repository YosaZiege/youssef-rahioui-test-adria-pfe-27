package rahioui.youssef.chatbot_service;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class EmptyMcpToolsTestConfiguration {
  @Bean
  ToolCallbackProvider mcpTools() {
    return ToolCallbackProvider.from();
  }
}
