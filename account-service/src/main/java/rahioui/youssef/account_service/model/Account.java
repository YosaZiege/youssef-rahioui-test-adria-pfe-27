package rahioui.youssef.account_service.model;

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
  private Long Id;

  private String customerReference;
  private Boolean status;
  private String currency;
  private Double balance;

}
