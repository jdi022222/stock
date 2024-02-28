package site.comibird.stock.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import site.comibird.stock.domain.Stock;
import site.comibird.stock.repository.StockRepository;

@SpringBootTest
class StockServiceTest {

	@Autowired
	private StockService stockService;

	@Autowired
	private StockRepository stockRepository;

	@BeforeEach
	void setUp() {
		stockRepository.saveAndFlush(new Stock(1L, 100L));
	}

	@AfterEach
	void tearDown() {
		stockRepository.deleteAll();
	}

	@Test
	void 재고감소() throws Exception {
		stockService.decrease(1L, 1L);
		Stock stock = stockRepository.findById(1L).orElseThrow();
		assertEquals(stock.getQuantity(), 99L);
	}
}