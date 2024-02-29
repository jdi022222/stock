package site.comibird.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import site.comibird.stock.domain.Stock;
import site.comibird.stock.repository.StockRepository;

@Service
public class StockService {

	private final StockRepository stockRepository;

	public StockService(StockRepository stockRepository) {
		this.stockRepository = stockRepository;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void decrease(Long id, Long quantity) {
		Stock stock = stockRepository.findById(id).orElseThrow(() -> new RuntimeException("Stock not found"));
		stock.decrease(quantity);
	}
}
