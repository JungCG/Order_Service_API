package ssg.osd.osapi;

import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.osd.osapi.domain.Order;
import ssg.osd.osapi.domain.OrderItem;
import ssg.osd.osapi.domain.Product;
import ssg.osd.osapi.repository.OrderItemRepository;
import ssg.osd.osapi.repository.OrderRepository;
import ssg.osd.osapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OsapiApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(OsapiApplicationTests.class);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	private Long testOrderId;

	@BeforeEach
	void setUp() {
		// 1. 테스트 상품 저장
		Product product = Product.builder()
				.id(1000000010L)
				.name("테스트 상품")
				.price(800)
				.discount(100)
				.stock(1000)
				.build();
		productRepository.save(product);

		// 2. 주문 생성
		Order order = new Order(LocalDateTime.now(), 1400);

		orderRepository.save(order);

		// 3. 주문 상품 생성
		OrderItem item = OrderItem.builder()
				.order(order)
				.product(product)
				.quantity(2)
				.totalPrice(1400) // 800 - 100
				.build();
		orderItemRepository.save(item);

		testOrderId = order.getId();
		logger.info("### 데이터 초기화 완료, 테스트 주문 ID: {}", testOrderId);
	}

	@AfterEach
	void tearDown() {
		orderItemRepository.deleteAll();
		orderRepository.deleteAll();
		productRepository.deleteAll();
	}

	@Test
	@DisplayName("주문 조회 API - 정상 조회")
	void getOrder_Success() throws Exception {
		mockMvc.perform(get("/api/orders/{orderId}", testOrderId)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderId").value(testOrderId))
				.andExpect(jsonPath("$.items").isArray())
				.andExpect(jsonPath("$.items[0].productId").value(1000000010L));

		logger.info("### 주문 조회 API 테스트 성공");
	}

	@Test
	@DisplayName("주문 조회 API - 존재하지 않는 주문번호")
	void getOrder_NotFound() throws Exception {
		mockMvc.perform(get("/api/orders/{orderId}", testOrderId+1)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		logger.info("### 존재하지 않는 주문번호 조회 API 테스트 성공");
	}

	@Test
	@DisplayName("주문 생성 API - 정상 주문")
	void createOrder_Success() throws Exception {
		String requestJson = """
        {
            "items": [
                { "productId": 1000000010, "quantity": 2 }
            ]
        }
    """;

		mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderId").exists())
				.andExpect(jsonPath("$.totalAmount").value(1400)); // (800 - 100) * 2

		logger.info("### 주문 생성 API 테스트 성공");
	}

	@Test
	@DisplayName("주문 생성 API - 재고 부족으로 실패")
	void createOrder_StockFailure() throws Exception {
		String requestJson = """
        {
            "items": [
                { "productId": 1000000010, "quantity": 1001 }
            ]
        }
    """;

		mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
				.andExpect(status().isBadRequest());

		logger.info("### 재고 부족으로 주문 실패 테스트 성공");
	}

	@Test
	@DisplayName("주문 상품 취소 API - 정상 처리")
	void cancelOrderItem_Success() throws Exception {
		mockMvc.perform(delete("/api/orders/{orderId}/items/{productId}", testOrderId, 1000000010))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.refundAmount").value(1400));

		logger.info("### 주문 상품 취소 API 테스트 성공");
	}

	@Test
	@DisplayName("주문 상품 취소 API - 존재하지 않는 주문번호")
	void cancelOrderItem_NotFound() throws Exception {
		mockMvc.perform(delete("/api/orders/{orderId}/items/{productId}", testOrderId+1, 1000000010))
				.andExpect(status().isBadRequest());

		logger.info("### 존재하지 않는 주문번호 취소 API 테스트 성공");
	}

	@Test
	@DisplayName("주문 상품 취소 API - 기취소된 상품")
	void cancelOrderItem_CanceledOrder() throws Exception {
		mockMvc.perform(delete("/api/orders/{orderId}/items/{productId}", testOrderId, 1000000010))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.refundAmount").value(1400));

		mockMvc.perform(delete("/api/orders/{orderId}/items/{productId}", testOrderId, 1000000010))
				.andExpect(status().isBadRequest());

		logger.info("### 기취소된 상품 취소 API 테스트 성공");
	}
}
