package rahioui.youssef.transaction_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rahioui.youssef.transaction_service.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
