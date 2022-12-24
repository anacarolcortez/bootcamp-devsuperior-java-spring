package com.heapster.dscatalog.controllers;

import com.heapster.dscatalog.dtos.CategoryDTO;
import com.heapster.dscatalog.entities.Category;
import com.heapster.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController()
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity findAll(){
        List<CategoryDTO> categories = service.findAll();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        CategoryDTO categoryDTO = service.findById(id);
        return ResponseEntity.ok().body(categoryDTO);
    }

    @PostMapping
    public ResponseEntity insert(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO category = service.insert(categoryDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(category.getId())
                .toUri();
        return ResponseEntity.created(uri).body(category);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
        CategoryDTO category = service.update(id, categoryDTO);
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
