package rahioui.youssef.transaction_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import rahioui.youssef.transaction_service.service.AccountOperations;

@SpringBootTest(properties = "eureka.client.enabled=false")
class TransactionServiceApplicationTests {

	@MockitoBean
	AccountOperations accountOperations;

	@Test
	void contextLoads() {
	}

}
