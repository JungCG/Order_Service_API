package ssg.osd.osapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssg.osd.osapi.dto.CancelOrderItemResponseDto;
import ssg.osd.osapi.dto.OrderDetailResponseDto;
import ssg.osd.osapi.dto.OrderRequestDto;
import ssg.osd.osapi.dto.OrderResponseDto;
import ssg.osd.osapi.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "주문 관련 API 목록")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "상품 ID와 수량으로 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder (@RequestBody OrderRequestDto request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @Operation(summary = "주문 조회", description = "주문 번호로 주문 상세를 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrderDetail (@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    @Operation(summary = "주문 상품 개별 취소", description = "주문 내 특정 상품을 개별 취소합니다.")
    @DeleteMapping("/{orderId}/items/{productId}")
    public ResponseEntity<CancelOrderItemResponseDto> cancelOrderItem (@PathVariable Long orderId, @PathVariable Long productId) {
        return ResponseEntity.ok(orderService.cancelOrderItem(orderId, productId));
    }
}
