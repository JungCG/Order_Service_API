package ssg.osd.osapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssg.osd.osapi.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
