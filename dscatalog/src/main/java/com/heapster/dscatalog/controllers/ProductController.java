package com.heapster.dscatalog.controllers;


import com.heapster.dscatalog.dtos.ProductDTO;
import com.heapster.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(value = "page", defaultValue="0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue="10") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue="ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue="name") String orderBy
    ){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<ProductDTO> products = service.findAllPaged(pageRequest);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        ProductDTO product = service.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO productDTO){
        ProductDTO product = service.insert(productDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productDTO){
        ProductDTO product = service.update(id, productDTO);
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
