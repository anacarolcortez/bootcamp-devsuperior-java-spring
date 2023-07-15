package com.heapster.dscatalog.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.heapster.dscatalog.dtos.ProductDTO;
import com.heapster.dscatalog.repositories.ProductRepository;
import com.heapster.dscatalog.services.ProductService;
import com.heapster.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional //faz rollback do BD para garantir que volte ao estado inicial antes de cada teste
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

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> response = service.findAllPaged(pageRequest);

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(0, response.getNumber());
        Assertions.assertEquals(10, response.getSize());
        Assertions.assertEquals(countTotalProducts, response.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> response = service.findAllPaged(pageRequest);

        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortedByName() {
        PageRequest pageRequest = PageRequest.of(50, 10, Sort.by("name"));

        Page<ProductDTO> response = service.findAllPaged(pageRequest);

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals("Macbook Pro", response.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", response.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", response.getContent().get(2).getName());
    }
    
}
