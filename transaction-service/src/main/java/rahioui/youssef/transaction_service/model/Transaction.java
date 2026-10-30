package rahioui.youssef.transaction_service.model;

import java.math.BigDecimal;
import java.sql.Date;

import org.hibernate.validator.constraints.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Transaction {

  /**
   *
   */
  @UUID
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;

  private UUID accountId;
  private BigDecimal amount;
  private String currency;
  private String merchantCategory;
  private String country;
  private Date createdAt;

}
