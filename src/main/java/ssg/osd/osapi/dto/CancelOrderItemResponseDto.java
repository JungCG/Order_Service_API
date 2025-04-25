package ssg.osd.osapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Schema(description = "주문 취소 상세 응답 DTO")
@AllArgsConstructor
public class CancelOrderItemResponseDto {
    @Schema(description = "상품 ID", example = "1000000001")
    private Long productId;
    @Schema(description = "상품명", example = "이마트 생수")
    private String productName;
    @Schema(description = "환불 금액", example = "700")
    private int refundAmount;
    @Schema(description = "남은 주문 금액", example = "0")
    private int remainingTotalAmount;
}
