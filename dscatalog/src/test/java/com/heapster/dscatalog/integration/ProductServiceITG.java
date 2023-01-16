package com.heapster.dscatalog.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.heapster.dscatalog.repositories.ProductRepository;
import com.heapster.dscatalog.services.ProductService;
import com.heapster.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class ProductServiceITG {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    public void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteProductWhenIdExists(){
        service.delete(existingId);

        Assertions.assertEquals(countTotalProducts-1, repository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }
    
}
