package ssg.osd.osapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 요청 DTO")
public class OrderRequestDto {
    @Schema(description = "주문 상품 리스트")
    private List<OrderItemRequest> items = new ArrayList<>();

    @Getter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "주문 상품 항목")
    public static class OrderItemRequest {
        @Schema(description = "상품 ID", example = "1000000001")
        private Long productId;
        @Schema(description = "수량", example = "2")
        private int quantity;
    }
}
