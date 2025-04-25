package ssg.osd.osapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssg.osd.osapi.dto.ProductResponseDto;
import ssg.osd.osapi.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "상품 재고 조회 API")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Operation(summary = "상품 재고 조회", description = "상품 재고 리스트를 조회합니다.")
    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> new ProductResponseDto(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getDiscount(),
                        p.getStock()
                )).toList();
    }
}
