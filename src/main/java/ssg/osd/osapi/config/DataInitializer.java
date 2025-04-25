package ssg.osd.osapi.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ssg.osd.osapi.domain.Product;
import ssg.osd.osapi.repository.ProductRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductRepository productRepository;

    @PostConstruct
    public void init() {
        productRepository.save(new Product(1000000001L, "이마트 생수", 800, 100, 1000));
        productRepository.save(new Product(1000000002L, "신라면 멀티팩", 4200, 500, 500));
        productRepository.save(new Product(1000000003L, "바나나 한송이", 3500, 300, 200));
        productRepository.save(new Product(1000000004L, "삼겹살 500g", 12000, 2000, 100));
        productRepository.save(new Product(1000000005L, "오리온 초코파이", 3000, 400, 300));
    }
}
