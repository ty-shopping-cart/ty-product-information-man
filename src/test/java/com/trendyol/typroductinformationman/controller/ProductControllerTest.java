package com.trendyol.typroductinformationman.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.typroductinformationman.dto.CategoryDto;
import com.trendyol.typroductinformationman.dto.CreateProductDto;
import com.trendyol.typroductinformationman.dto.ProductDto;
import com.trendyol.typroductinformationman.dto.UpdateProductDto;
import com.trendyol.typroductinformationman.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    ProductService productService;

    @BeforeEach
    void beforeEach(){
        if (mvc != null) {
            return;
        }

        this.mvc = MockMvcBuilders.standaloneSetup(new ProductController(productService))
                .build();
        objectMapper = new ObjectMapper();
    }


    @Test
    void getProduct_returnSuccess_whenEverythingIsOkay() throws Exception {

        Integer productId = 1;

        ProductDto productDto = new ProductDto();
        productDto.setBarcode("A123S");
        productDto.setImageURL("product1.jpg");
        productDto.setPrice(123.0);
        productDto.setTitle("product1");
        productDto.setCategoryId(1);
        productDto.setProductId(productId);

        productDto.setStock(50);

        Mockito.when(productService.getProduct(productId)).thenReturn(productDto);

        MvcResult mvcResult = mvc.perform(get("/products/?productId=" + productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductDto response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsByteArray(), ProductDto.class);

        assertThat(response.getCategoryId()).isEqualTo(1);
        assertThat(response.getTitle()).isEqualTo("product1");
    }

    @Test
    void createProduct_returnSuccess_whenEverythingIsOkay() throws Exception {

        CreateProductDto request = new CreateProductDto();
        request.setBarcode("A123S");
        request.setImageURL("product1.jpg");
        request.setPrice(123.0);
        request.setTitle("product1");
        request.setCategoryId(1);

        request.setStock(50);

        String expected = "product created";

        Mockito.when(productService.createProduct(request)).thenReturn(expected);

        MvcResult mvcResult = mvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void updateProduct_returnSuccess_whenEverythingIsOkay() throws Exception {

        UpdateProductDto request = new UpdateProductDto();
        request.setBarcode("A123S");
        request.setImageURL("product1.jpg");
        request.setPrice(123.0);
        request.setTitle("product1");
        request.setCategoryId(1);
        request.setProductId(1L);


        request.setStock(50);

        String expected = "product updated";

        Mockito.when(productService.updateProduct(request)).thenReturn(expected);

        MvcResult mvcResult = mvc.perform(put("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void createCategory_returnSuccess_whenEverythingIsOkay() throws Exception {

        CategoryDto request = new CategoryDto();
        request.setName("Telefon");

        String expected = "category created";

        Mockito.when(productService.addCategory(request)).thenReturn(expected);

        MvcResult mvcResult = mvc.perform(post("/products/createCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo(expected);
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
