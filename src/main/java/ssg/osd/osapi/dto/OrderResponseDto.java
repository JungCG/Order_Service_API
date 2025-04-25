package ssg.osd.osapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "주문 응답 DTO")
@AllArgsConstructor
public class OrderResponseDto {
    @Schema(description = "주문 ID")
    private Long orderId;
    @Schema(description = "주문 상품 리스트")
    private List<OrderItemResponse> items;
    @Schema(description = "주문 총 금액")
    private int totalAmount;

    @Getter
    @AllArgsConstructor
    @Schema(description = "주문 상품 리스트")
    public static class OrderItemResponse {
        @Schema(description = "상품 ID", example = "1000000001")
        private Long productId;
        @Schema(description = "수량", example = "2")
        private int quantity;
        @Schema(description = "상품 금액", example = "3500")
        private int totalPrice;
    }

}
