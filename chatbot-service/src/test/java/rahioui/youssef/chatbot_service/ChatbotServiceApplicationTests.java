package rahioui.youssef.chatbot_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(properties = {
    "spring.ai.openai.api-key=test-key",
    "spring.ai.mcp.client.enabled=false"
})
@Import(EmptyMcpToolsTestConfiguration.class)
class ChatbotServiceApplicationTests {
  @Test
  void contextLoads() {
  }
}
