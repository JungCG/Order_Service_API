package ssg.osd.osapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssg.osd.osapi.domain.Order;
import ssg.osd.osapi.domain.OrderItem;
import ssg.osd.osapi.domain.Product;
import ssg.osd.osapi.dto.CancelOrderItemResponseDto;
import ssg.osd.osapi.dto.OrderDetailResponseDto;
import ssg.osd.osapi.dto.OrderRequestDto;
import ssg.osd.osapi.dto.OrderResponseDto;
import ssg.osd.osapi.repository.OrderRepository;
import ssg.osd.osapi.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponseDto createOrder (OrderRequestDto request) {
        List<OrderItem> orderItems = request.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다 : " + item.getProductId()));

            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("재고가 부족합니다: "+product.getName());
            }

            product.decreaseStock(item.getQuantity());
            return new OrderItem(product, item.getQuantity());
        }).collect(Collectors.toList());

        Order order = new Order(orderItems);
        orderRepository.save(order);

        List<OrderResponseDto.OrderItemResponse> itemResponses = order.getItems().stream()
                .map(oi -> new OrderResponseDto.OrderItemResponse(
                        oi.getProduct().getId(),
                        oi.getQuantity(),
                        oi.getTotalPrice()
                ))
                .collect(Collectors.toList());

        return new OrderResponseDto(order.getId(), itemResponses, order.getTotalAmount());
    }

    @Transactional
    public OrderDetailResponseDto getOrderDetail (Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다: "+orderId));

        List<OrderDetailResponseDto.OrderItemDto> itemDtos = order.getItems().stream()
                .map(oi -> new OrderDetailResponseDto.OrderItemDto(
                        oi.getProduct().getId(),
                        oi.getProduct().getName(),
                        oi.getQuantity(),
                        oi.getTotalPrice(),
                        oi.isCancelled()
                ))
                .collect(Collectors.toList());

        return new OrderDetailResponseDto(order.getId(), itemDtos, order.getTotalAmount());
    }

    @Transactional
    public CancelOrderItemResponseDto cancelOrderItem (Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다: "+orderId));

        OrderItem item = order.getItems().stream()
                .filter(oi -> oi.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 주문에 상품이 존재하지 않습니다: "+productId));

        if(item.isCancelled()) {
            throw new IllegalStateException("이미 취소된 상품입니다.");
        }

        item.cancel();
        order.removeItem(item);

        return new CancelOrderItemResponseDto(
                productId,
                item.getProduct().getName(),
                item.getTotalPrice(),
                order.getTotalAmount()
        );
    }
}
