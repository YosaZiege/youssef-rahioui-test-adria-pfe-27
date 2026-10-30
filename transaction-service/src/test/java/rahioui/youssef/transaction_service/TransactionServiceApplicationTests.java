package rahioui.youssef.transaction_service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.modelcontextprotocol.server.McpSyncServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import rahioui.youssef.transaction_service.service.AccountOperations;

@SpringBootTest(properties = "eureka.client.enabled=false")
class TransactionServiceApplicationTests {
	@Autowired
	McpSyncServer mcpServer;

	@MockitoBean
	AccountOperations accountOperations;

	@Test
	void contextLoads() {
		assertTrue(mcpServer.listTools().stream().anyMatch(tool -> tool.name().equals("get_transaction")));
		assertTrue(mcpServer.listTools().stream().anyMatch(tool -> tool.name().equals("create_transaction")));
	}

}
