package ssg.osd.osapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "주문 상세 응답 DTO")
@AllArgsConstructor
public class OrderDetailResponseDto {
    @Schema(description = "주문 ID")
    private Long orderId;
    @Schema(description = "주문 리스트")
    private List<OrderItemDto> items;
    @Schema(description = "상품 ID")
    private int totalAmount;

    @Getter
    @AllArgsConstructor
    public static class OrderItemDto {
        @Schema(description = "상품 ID", example = "1000000001")
        private Long productId;
        @Schema(description = "상품명", example = "이마트 생수")
        private String productName;
        @Schema(description = "주문수량", example = "1")
        private int quantity;
        @Schema(description = "상품금액", example = "700")
        private int totalPrice;
        @Schema(description = "취소여부", example = "true")
        private boolean cancelled;
    }
}
