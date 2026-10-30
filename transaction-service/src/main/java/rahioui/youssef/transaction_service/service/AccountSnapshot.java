package rahioui.youssef.transaction_service.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountSnapshot(UUID id, String status, String currency, BigDecimal balance) {
}
