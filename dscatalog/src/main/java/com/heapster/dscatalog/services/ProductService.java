package com.heapster.dscatalog.services;

import com.heapster.dscatalog.dtos.ProductDTO;
import com.heapster.dscatalog.entities.Product;
import com.heapster.dscatalog.repositories.ProductRepository;
import com.heapster.dscatalog.services.exceptions.DataBaseException;
import com.heapster.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> products = repository.findAll(pageRequest);
        return products.map(p -> new ProductDTO(p, p.getCategories()));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> obj = repository.findById(id);
        Product product = obj.orElseThrow(() -> new ResourceNotFoundException("Product id not found " + id));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO){
        Product product = new Product();
        product = repository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO){
        try {
            Product product = repository.getReferenceById(id);
            product = repository.save(product);
            return new ProductDTO(product);
        } catch (EntityNotFoundException err){
            throw new ResourceNotFoundException("Id not found" + id);
        }
    }

    public void delete(Long id){
        try{
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException err){
            throw new ResourceNotFoundException("Id not found" + id);
        } catch (DataIntegrityViolationException err) {
            throw new DataBaseException("Integrity violation");
        }
    }

}
