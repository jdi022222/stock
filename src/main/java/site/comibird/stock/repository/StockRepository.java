package site.comibird.stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import site.comibird.stock.domain.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select s from Stock s where s.id = :id")
	Optional<Stock> findByIdWithPessimisticLock(Long id);

	@Lock(value = LockModeType.OPTIMISTIC)
	@Query("select s from Stock s where s.id = :id")
	Optional<Stock> findByIdWithOptimisticLock(Long id);
}
