# 🛒 주문 서비스 API 구현 프로젝트

---

## ✅ 주요 기능

### 1. 주문 생성 API
- 상품 ID와 수량 리스트를 입력받아 주문을 생성합니다.
- 각 상품의 실구매 금액과 주문 전체 금액을 반환합니다.
- 재고가 부족하면 주문이 실패하며, 주문 생성 시 재고가 차감됩니다.

📸 주문 생성 요청 Swagger 캡처  
![create-order-swagger](./Images/swagger1.png)
![create-order](./Images/image1.png)

📸 주문 생성 응답 결과  
![create-order-swagger-response](./Images/swagger2.png)
![create-order-response](./Images/image2.png)

---

### 2. 주문 상품 개별 취소 API
- 주문번호와 취소할 상품 ID를 입력받아 해당 상품을 취소합니다.
- 취소된 상품 정보, 환불 금액, 취소 후 남은 전체 주문 금액을 반환합니다.
- 재고는 복구되며, 이미 취소된 상품이나 잘못된 ID 요청 시 에러 처리됩니다.

📸 주문 상품 취소 요청 Swagger 캡처  
![cancel-item-swagger](./Images/swagger3.png)
![cancel-item](./Images/image3.png)

📸 주문 상품 취소 응답 결과  
![cancel-item-swagger-response](./Images/swagger4.png)
![cancel-item-response](./Images/image4.png)

---

### 3. 주문 조회 API
- 주문번호를 입력받아 주문된 상품들의 정보(수량, 실구매 금액, 총 주문 금액)를 반환합니다.
- 존재하지 않는 주문번호 입력 시 에러 처리됩니다.

📸 주문 조회 요청 Swagger 캡처  
![get-order-swagger](./Images/swagger5.png)

📸 주문 조회 응답 결과  
![get-order-swagger-response](./Images/swagger6.png)

---

# 📦 프로젝트 구조

## 1. 📁 **config** - 설정 관련 파일들
- `📄 DataInitializer.java`  : 초기 데이터 로딩 설정 클래스
- `📄 SwaggerConfig.java`    : Swagger API 문서 설정 클래스

## 2. 🌐 **controller** - 웹 요청을 처리하는 컨트롤러
- `📄 OrderController.java`  : 주문 관련 API 컨트롤러
- `📄 ProductController.java` : 상품 관련 API 컨트롤러
- `🖼️ OrderUiController.java` : 주문 UI 테스트용 HTML 컨트롤러

## 3. 🏷️ **domain** - 도메인 모델 (엔티티)
- `📄 Order.java`            : 주문 엔티티
- `📄 OrderItem.java`        : 주문 상품 엔티티
- `📄 Product.java`          : 상품 엔티티

## 4. 🗂️ **dto** - 데이터 전송 객체 (DTO)
- `🧾 OrderRequestDto.java`  : 주문 요청 DTO
- `🧾 OrderResponseDto.java` : 주문 응답 DTO
- `🧾 CancelForm.java`       : 주문 상품 취소 요청 DTO

## 5. 💥 **exception** - 예외 처리
- `📄 GlobalExceptionHandler.java` : 전역 예외 처리 핸들러

## 6. 🧠 **service** - 비즈니스 로직
- `📄 OrderService.java`      : 주문 비즈니스 로직

## 7. 🧪 **test** - 테스트 코드
- `📄 OsapiApplicationTests.java` : 기본 애플리케이션 테스트

## 8. 📄 **resources** - 리소스 파일
- `📄 application.yml`        : 애플리케이션 설정 파일
- `🖼️ templates/order-ui.html` : 주문 UI 화면 템플릿

## 9. 🛠️ **기타 파일**
- `📄 build.gradle`           : Gradle 빌드 설정 파일


---

## ⚙️ 기술 스택

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA**
- **H2 In-Memory Database**
- **Swagger UI**
- **JUnit + Mockito**

---

## 🧪 JUnit 테스트 코드

- 서비스 로직 중심의 단위 테스트 및 통합 테스트 작성
- 재고 부족, 주문 취소 등 예외 상황도 포함한 테스트 시나리오 구성

![H2 Console](./Images/test.png)

<details> <summary>📌 테스트 코드</summary>

```java
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

```

</details>

---

## 📑 API 문서 (Swagger)

- `http://localhost:5537/swagger-ui/index.html` 접속 시 API 문서를 확인할 수 있습니다.
- 모든 필수 API에 대한 요청/응답 예시 및 설명이 포함되어 있습니다.

---

## 🗂 테스트 URL
- H2 Console : http://localhost:5537/h2-console/login.jsp
  ![H2 Console](./Images/ui1.png)
- Swagger Web : http://localhost:5537/swagger-ui/index.html
  ![Swagger Web](./Images/ui2.png)
- 통합 주문 UI : http://localhost:5537/ui
  ![Order Web](./Images/ui3.png)