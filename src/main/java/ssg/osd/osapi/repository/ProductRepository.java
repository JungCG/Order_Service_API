package ssg.osd.osapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssg.osd.osapi.domain.Product;

public interface ProductRepository extends JpaRepository <Product, Long> {
}
