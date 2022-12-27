package com.heapster.dscatalog.services;

import com.heapster.dscatalog.dtos.CategoryDTO;
import com.heapster.dscatalog.entities.Category;
import com.heapster.dscatalog.repositories.CategoryRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

//    @Transactional(readOnly = true)
//    public List<CategoryDTO> findAll(){
//        List<Category> categories = repository.findAll();
//        return categories.stream().map(c -> new CategoryDTO(c)).collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> categories = repository.findAll(pageRequest);
        return categories.map(c -> new CategoryDTO(c));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        Optional<Category> obj = repository.findById(id);
        Category category = obj.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category = repository.save(category);
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try{
            Category category = repository.getReferenceById(id);
            category.setName(categoryDTO.getName());
            category = repository.save(category);
            return new CategoryDTO(category);
        } catch (EntityNotFoundException err){
            throw new ResourceNotFoundException("Id not found" + id);
        }
    }

    public void delete(Long id) {
        try{
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException err){
            throw new ResourceNotFoundException("Id not found" + id);
        } catch (DataIntegrityViolationException err) {
            throw new DataBaseException("Integrity violation");
        }
    }

}
