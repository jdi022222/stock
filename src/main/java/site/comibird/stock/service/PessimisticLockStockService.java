package site.comibird.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.comibird.stock.domain.Stock;
import site.comibird.stock.repository.StockRepository;

@Service
public class PessimisticLockStockService {

	private final StockRepository stockRepository;

	public PessimisticLockStockService(StockRepository stockRepository) {
		this.stockRepository = stockRepository;
	}

	@Transactional
	public void decrease(Long id, Long quantity) {
		Stock stock = stockRepository.findByIdWithPessimisticLock(id).orElseThrow(() -> new RuntimeException("Stock not found"));
		stock.decrease(quantity);
	}
}
