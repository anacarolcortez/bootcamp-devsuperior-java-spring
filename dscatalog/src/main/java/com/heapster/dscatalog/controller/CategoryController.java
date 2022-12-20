package com.heapster.dscatalog.controller;

import com.heapster.dscatalog.entities.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping(value = "/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity findAll(){
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(123L,"Books"));
        categories.add(new Category(321L, "Electronics"));
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
