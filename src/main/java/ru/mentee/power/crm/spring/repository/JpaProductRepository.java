package ru.mentee.power.crm.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.spring.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findBySku(String sku);
    // Spring Data JPA автоматически сгенерирует реализацию

    List<Product> findByActiveTrue();
}
