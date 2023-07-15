package com.heapster.dscatalog.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heapster.dscatalog.dtos.ProductDTO;
import com.heapster.dscatalog.factory.Factory;
import com.heapster.dscatalog.services.ProductService;
import com.heapster.dscatalog.services.exceptions.DataBaseException;
import com.heapster.dscatalog.services.exceptions.ResourceNotFoundException;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mocMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        productDTO = Factory.createProductDTO();

        page = new PageImpl<>(List.of(productDTO));
        Mockito.when(service.findAllPaged(any())).thenReturn(page);

        Mockito.when(service.findById(existingId)).thenReturn(productDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.update(eq(existingId), any())).thenReturn(productDTO);
        Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        // quando o método da dependência mockada retorna void, inverte-se a sintaxe do
        // mockito (when no final)
        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        Mockito.doThrow(DataBaseException.class).when(service).delete(dependentId);

        Mockito.when(service.insert(any())).thenReturn(productDTO);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mocMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mocMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        ResultActions result = mocMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductWhenIdExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mocMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mocMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mocMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        ResultActions result = mocMvc.perform(delete("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnDatabaseExceptionWhenIdDoesNotExist() throws Exception {
        ResultActions result = mocMvc.perform(delete("/products/{id}", dependentId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void createShoulReturnProductDTOWhenIdDoesNotExist() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mocMvc.perform(post("/products", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

}
