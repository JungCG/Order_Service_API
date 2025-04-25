package ssg.osd.osapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "상품 재고 응답 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    @Schema(description = "상품 ID", example = "1000000001")
    private Long productId;
    @Schema(description = "상품명", example = "이마트 생수")
    private String name;
    @Schema(description = "판매가격(원)", example = "800")
    private int price;
    @Schema(description = "할인금액(원)", example = "100")
    private int discount;
    @Schema(description = "재고", example = "1000")
    private int stock;
}
