package com.heapster.dscatalog.controller;

import com.heapster.dscatalog.entities.Category;
import com.heapster.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity findAll(){
        List<Category> categories = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
