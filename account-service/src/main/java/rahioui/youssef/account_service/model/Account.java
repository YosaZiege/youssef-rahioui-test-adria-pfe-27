package rahioui.youssef.account_service.model;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Account {

  @UUID
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID Id;

  private String customerReference;
  private AccountStatus status;
  private String currency;
  private BigDecimal balance;

}
