package rahioui.youssef.transaction_service.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Transaction {

  /**
   *
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private UUID accountId;
  private BigDecimal amount;
  private String currency;
  private String merchantCategory;
  private String country;
  private Instant createdAt;

}
