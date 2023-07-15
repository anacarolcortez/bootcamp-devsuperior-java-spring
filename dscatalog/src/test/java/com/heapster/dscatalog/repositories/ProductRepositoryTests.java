package com.heapster.dscatalog.repositories;

import com.heapster.dscatalog.entities.Product;
import com.heapster.dscatalog.factory.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {
    private Long existingId;
    private Long nonExistingID;

    private Long countTotalProducts;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingID = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldCreateNewPoductWithAutoIncrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts+1, product.getId());
    }

    public void updateShouldUpdatePoductWhenIdExists(){
        String url = "http://imgur.testeimg.jpg";
        Product product = Factory.createProduct();
        product.setId(existingId);
        product.setImgUrl(url);
        product = repository.save(product);

        Assertions.assertEquals(existingId, product.getId());
        Assertions.assertEquals(url, product.getImgUrl());
    }

    @Test
    public void findShouldReturnOptionalProductWhenIdExists(){
        Optional<Product> product = repository.findById(existingId);
        Assertions.assertTrue(product.isPresent());
        Assertions.assertEquals(Optional.class, product.getClass());
    }

    @Test
    public void findShouldReturnOptionalProductEmptyWhenIdDoesNotExist(){
        Optional<Product> product = repository.findById(nonExistingID);
        Assertions.assertTrue(product.isEmpty());
        Assertions.assertEquals(Optional.class, product.getClass());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        repository.deleteById(existingId);
        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingID);
        });
    }

}
