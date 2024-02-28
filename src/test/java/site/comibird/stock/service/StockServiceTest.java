package site.comibird.stock.service;

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
class StockServiceTest {

	@Autowired
	private PessimisticLockStockService stockService;

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
		assertEquals(99L, stock.getQuantity());
	}

	@Test
	void 동시에_100개의_재고감소() throws Exception {
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);// 비동기 작업을 단순화
		CountDownLatch latch = new CountDownLatch(threadCount); // 다른 스레드에서 작업이 완료될 때까지 기다릴 수 있도록 해주는 클래스

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					stockService.decrease(1L, 1L);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		Stock stock = stockRepository.findById(1L).orElseThrow();
		assertEquals(0L, stock.getQuantity());
	}
}