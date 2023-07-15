package com.heapster.dscatalog.repositories;

import com.heapster.dscatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
