package ssg.osd.osapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssg.osd.osapi.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
