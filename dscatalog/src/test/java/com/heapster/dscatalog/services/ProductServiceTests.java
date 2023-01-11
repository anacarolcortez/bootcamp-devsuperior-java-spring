package com.heapster.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.heapster.dscatalog.entities.Category;
import com.heapster.dscatalog.entities.Product;
import com.heapster.dscatalog.dtos.CategoryDTO;
import com.heapster.dscatalog.dtos.ProductDTO;
import com.heapster.dscatalog.factory.Factory;
import com.heapster.dscatalog.repositories.CategoryRepository;
import com.heapster.dscatalog.repositories.ProductRepository;
import com.heapster.dscatalog.services.exceptions.DataBaseException;
import com.heapster.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
    
    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 2L;
        product = Factory.createProduct();
        productDTO =  Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));
        category = Factory.createCategory();
        categoryDTO = Factory.createCategoryDTO();

        //simulando o comportamento real do repositório, que foi mockado para o teste unitário
        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);
        Mockito.doThrow(DataBaseException.class).when(repository).deleteById(dependentId);

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
    
    }


    @Test
    public void deleteShouldNotThrowErrorWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        Mockito.verify(repository).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowEmptyResultExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });

        Mockito.verify(repository).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenIdDependsOnAnotherId(){
        Assertions.assertThrows(DataBaseException.class, () -> {
            service.delete(dependentId);
        });

        Mockito.verify(repository).deleteById(dependentId);
    }

    @Test
    public void findAllShouldReturnPageOfProducts(){
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);

        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() {
        ProductDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);

        Mockito.verify(repository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });

        Mockito.verify(repository).findById(nonExistingId);

    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists(){
        ProductDTO productDTO = service.update(existingId, this.productDTO);
        Assertions.assertNotNull(productDTO);

        Mockito.verify(repository).getReferenceById(existingId);
        Mockito.verify(categoryRepository).getReferenceById(existingId);

    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, productDTO);
        });

        Mockito.verify(repository).getReferenceById(nonExistingId);

    }

    @Test
    public void saveShouldReturnProductDTOWhenIdDoesNotExist(){

    }

}
