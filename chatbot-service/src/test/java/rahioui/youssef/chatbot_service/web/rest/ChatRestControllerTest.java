package rahioui.youssef.chatbot_service.web.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import rahioui.youssef.chatbot_service.service.ChatService;
import rahioui.youssef.chatbot_service.EmptyMcpToolsTestConfiguration;

@SpringBootTest(properties = {
    "spring.ai.openai.api-key=test-key",
    "spring.ai.mcp.client.enabled=false"
})
@AutoConfigureMockMvc
@Import(EmptyMcpToolsTestConfiguration.class)
class ChatRestControllerTest {
  @Autowired
  MockMvc mvc;

  @MockitoBean
  ChatService chatService;

  @Test
  void returnsAnswerForJsonRequest() throws Exception {
    when(chatService.reply("What is the balance?")).thenReturn("The balance is 500 EUR.");

    mvc.perform(post("/chat")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"message\":\"What is the balance?\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.answer").value("The balance is 500 EUR."));
  }
}
