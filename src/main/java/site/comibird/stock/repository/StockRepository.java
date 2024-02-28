package site.comibird.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.comibird.stock.domain.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}
