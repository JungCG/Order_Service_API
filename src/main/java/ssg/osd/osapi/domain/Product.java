package ssg.osd.osapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class Product {

    @Id
    private Long id;

    private String name;
    private int price;
    private int discount;
    private int stock;

    public Product (Long id, String name, int price, int discount, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
    }

    public void decreaseStock (int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고 부족");
        }

        this.stock -= quantity;
    }

    public void increaseStock (int quantity) {
        this.stock += quantity;
    }

    public int getDiscountedPrice() {
        return this.price - this.discount;
    }
}
