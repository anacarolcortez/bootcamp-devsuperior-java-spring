package com.heapster.dscatalog.factory;

import com.heapster.dscatalog.dtos.ProductDTO;
import com.heapster.dscatalog.entities.Category;
import com.heapster.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Great Phone", 1800.0, "https://imgur.igsjed3.jpeg", Instant.parse("2020-10-20T03:03:00Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

}
