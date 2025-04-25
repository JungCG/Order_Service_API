package ssg.osd.osapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private int totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order (List<OrderItem> items) {
        this.createdAt = LocalDateTime.now();

        this.items = items;

        for (OrderItem item : items) {
            item.setOrder(this);
        }
        this.totalAmount = items.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

    public Order(LocalDateTime now, int totalAmount) {
        this.createdAt = now;
        this.totalAmount = totalAmount;
    }

    public void removeItem (OrderItem item) {
        this.items.remove(item);

        item.setCancelled(true);
        this.items.add(item);

        this.totalAmount -= item.getTotalPrice();
    }
}
