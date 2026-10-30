package rahioui.youssef.chatbot_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;

class ChatServiceTest {
  @Test
  void passesMcpToolsToChatClient() {
    ChatClient.Builder builder = mock(ChatClient.Builder.class, RETURNS_SELF);
    ChatClient client = mock(ChatClient.class);
    ToolCallbackProvider tools = ToolCallbackProvider.from();
    when(builder.build()).thenReturn(client);

    assertEquals(client, new ChatConfiguration().bankChatClient(builder, tools));
    verify(builder).defaultToolCallbacks(tools);
  }

  @Test
  void sendsQuestionToModelAndReturnsItsAnswer() {
    ChatClient client = mock(ChatClient.class, RETURNS_DEEP_STUBS);
    ChatService service = new ChatService(client);
    when(client.prompt().user("Balance?").call().content()).thenReturn("500 EUR");

    assertEquals("500 EUR", service.reply("Balance?"));
    assertThrows(IllegalArgumentException.class, () -> service.reply(" "));
  }
}
