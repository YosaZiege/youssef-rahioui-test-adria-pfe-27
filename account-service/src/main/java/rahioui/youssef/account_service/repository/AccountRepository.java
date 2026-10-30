package rahioui.youssef.account_service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import rahioui.youssef.account_service.model.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {

}
