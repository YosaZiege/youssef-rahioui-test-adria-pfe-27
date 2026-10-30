package rahioui.youssef.account_service.model;

import java.math.BigDecimal;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String customerReference;

  @Enumerated(EnumType.STRING)
  private AccountStatus status;

  private String currency;
  private BigDecimal balance;

  @Version
  private Long version;
}
