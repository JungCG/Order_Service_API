package ssg.osd.osapi.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderForm {
    private List<ItemForm> items = new ArrayList<>();

    @Data
    public static class ItemForm {
        private Long productId;
        private int quantity;
    }

    public OrderRequestDto toDto() {
        List<OrderRequestDto.OrderItemRequest> dtoItems = items.stream()
                .map(i -> new OrderRequestDto.OrderItemRequest(i.getProductId(), i.getQuantity()))
                .collect(Collectors.toList());

        return new OrderRequestDto(dtoItems);
    }
}
