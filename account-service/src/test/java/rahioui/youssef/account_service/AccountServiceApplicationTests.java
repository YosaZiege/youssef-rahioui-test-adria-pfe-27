package rahioui.youssef.account_service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.modelcontextprotocol.server.McpSyncServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "eureka.client.enabled=false")
class AccountServiceApplicationTests {
	@Autowired
	McpSyncServer mcpServer;

	@Test
	void contextLoads() {
		assertTrue(mcpServer.listTools().stream().anyMatch(tool -> tool.name().equals("get_account")));
		assertTrue(mcpServer.listTools().stream().anyMatch(tool -> tool.name().equals("get_account_balance")));
	}

}
