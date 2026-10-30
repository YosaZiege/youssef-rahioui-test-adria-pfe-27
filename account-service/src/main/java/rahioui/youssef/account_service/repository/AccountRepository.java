package rahioui.youssef.account_service.repository;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import rahioui.youssef.account_service.model.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {

}
