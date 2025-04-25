package ssg.osd.osapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private int quantity;
    private int totalPrice;

    @Builder.Default
    private boolean cancelled = false;

    public OrderItem (Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = product.getDiscountedPrice() * quantity;
    }

    public void setOrder (Order order) {
        this.order = order;
    }

    public void cancel(){
        if(this.cancelled) {
            throw new IllegalArgumentException("기취소 상품");
        }

        this.cancelled = true;
        product.increaseStock(quantity);
    }
}
