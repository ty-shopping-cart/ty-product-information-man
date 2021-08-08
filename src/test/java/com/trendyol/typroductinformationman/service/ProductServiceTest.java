package com.trendyol.typroductinformationman.service;

import com.trendyol.typroductinformationman.dto.*;
import com.trendyol.typroductinformationman.model.Category;
import com.trendyol.typroductinformationman.model.Product;
import com.trendyol.typroductinformationman.model.ProductStock;
import com.trendyol.typroductinformationman.repository.CategoryRepository;
import com.trendyol.typroductinformationman.repository.ProductRepository;
import com.trendyol.typroductinformationman.repository.ProductStockRepository;
import com.trendyol.typroductinformationman.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductStockRepository productStockRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserService userService;

    @Mock
    Environment environment;

    @Mock
    KafkaTemplate kafkaTemplate;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ProductServiceImpl productService;

    @ParameterizedTest
    @ValueSource(ints = {1})
    void getProduct_returnSuccess_whenEverythingIsOkay(Integer productId) {

        Category category = new Category();
        category.setName("category1");
        category.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setTitle("product1");
        product.setImageURL("asd.jpg");
        product.setCategory(category);

        ProductStock productStock = new ProductStock();
        productStock.setStockQuantity(100);
        productStock.setProduct(product);
        productStock.setId(1L);

        when(productRepository.findById(productId.longValue())).thenReturn(java.util.Optional.of(product));
        when(productStockRepository.findByProduct_Id(productId.longValue())).thenReturn(productStock);

        ProductDto response = productService.getProduct(productId);

        assertThat(response.getProductId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("product1");
        assertThat(response.getStock()).isEqualTo(100);
    }

    @Test
    void createProduct_returnSuccess_whenEverythingIsOkay() {

        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setBarcode("A123B");
        createProductDto.setCategoryId(1);
        createProductDto.setImageURL("product3.jpg");
        createProductDto.setPrice(150.50);
        createProductDto.setStock(100);
        createProductDto.setTitle("Product1");

        Category category = new Category();
        category.setId(1L);
        category.setName("telefon");

        when(categoryRepository.findById(Long.valueOf(createProductDto.getCategoryId()))).thenReturn(java.util.Optional.of(category));

        productService.createProduct(createProductDto);

        Mockito.verify(productRepository, Mockito.times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_returnSuccess_whenEverythingIsOkay() {
        UpdateProductDto request = new UpdateProductDto();
        request.setBarcode("A123B");
        request.setCategoryId(1);
        request.setImageURL("product3.jpg");
        request.setPrice(150.50);
        request.setStock(2);
        request.setTitle("Product1");
        request.setProductId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(200.0);
        product.setTitle("Product1");
        when(productRepository.findById(request.getProductId())).thenReturn(java.util.Optional.of(product));

        String url = "http://localhost:8080/cart/fetchUsersWithProductsInCart/?productId=";

        when(environment.getProperty("cart.fetchUsersWithProductsInCart")).thenReturn(url);

        when(restTemplate.getForObject(url + 1 ,String.class)).thenReturn("[10]");


        List<Integer> userList = new ArrayList<>();
        userList.add(10);

        UserDto userDto = new UserDto();
        userDto.setUserId(10);
        userDto.setEmail("test@gmail.com");
        userDto.setName("testUser");

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);

        when(userService.getUserList(userList)).thenReturn(userDtoList);

        ProductStock productStock = new ProductStock();
        productStock.setId(1L);
        productStock.setStockQuantity(100);

        when(productStockRepository.findByProduct_Id(request.getProductId().longValue())).thenReturn(productStock);

        productService.updateProduct(request);

        Mockito.verify(productStockRepository, Mockito.times(1)).save(any(ProductStock.class));
        Mockito.verify(productRepository, Mockito.times(1)).save(any(Product.class));
        Mockito.verify(kafkaTemplate, Mockito.times(2)).send(any(String.class),any(UserMessageNotificationDto.class));
    }

    @Test
    void updateProductStock_returnSuccess_whenEverythingIsOkay() {

        ProductStockDto request = new ProductStockDto();
        request.setProductId(1);
        request.setStockQuantity(150);

        ProductStock productStock = new ProductStock();
        productStock.setProduct(new Product());
        productStock.setStockQuantity(100);

        when(productStockRepository.findByProduct_Id(request.getProductId().longValue())).thenReturn(productStock);

        productService.updateProductStock(request);

        Mockito.verify(productStockRepository, Mockito.times(1)).save(productStock);
    }

    @Test
    void addCategory_returnSuccess_whenEverythingIsOkay() {

        CategoryDto request = new CategoryDto();
        request.setName("Telefon");

        Category category = new Category();
        category.setName("Telefon");

        productService.addCategory(request);

        Mockito.verify(categoryRepository, Mockito.times(1)).save(any(Category.class));
    }
}
