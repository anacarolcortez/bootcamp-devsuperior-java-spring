package com.heapster.dscatalog.services;

import com.heapster.dscatalog.dtos.CategoryDTO;
import com.heapster.dscatalog.entities.Category;
import com.heapster.dscatalog.repositories.CategoryRepository;
import com.heapster.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> categories = repository.findAll();
        return categories.stream().map(c -> new CategoryDTO(c)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        Optional<Category> obj = repository.findById(id);
        Category category = obj.orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return new CategoryDTO(category);
    }

    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category = repository.save(category);
        return new CategoryDTO(category);
    }
}
