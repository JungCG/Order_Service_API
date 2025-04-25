package ssg.osd.osapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("상품 주문 REST API")
                        .version("v1.0.1")
                        .description("이 문서는 상품 주문, 조회, 취소 API를 정의합니다."));
    }
}