package site.comibird.stock.facade;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import site.comibird.stock.domain.Stock;
import site.comibird.stock.repository.StockRepository;

@SpringBootTest
class NamedLockStockFacadeTest {

	@Autowired
	private NamedLockStockFacade namedLockStockFacade;

	@Autowired
	private StockRepository stockRepository;

	@BeforeEach
	public void insert() {
		Stock stock = new Stock(1L, 100L);
		stockRepository.saveAndFlush(stock);
	}

	@AfterEach
	public void delete() {
		stockRepository.deleteAll();
	}

	@Test
	public void 동시에_100개의_재고감소() throws InterruptedException {
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					namedLockStockFacade.decrease(1L, 1L);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		Stock stock = stockRepository.findById(1L).orElseThrow();

		assertEquals(0, stock.getQuantity());
	}
}