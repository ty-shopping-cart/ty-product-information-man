package com.trendyol.typroductinformationman.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.typroductinformationman.domain.UserNotificationType;
import com.trendyol.typroductinformationman.dto.*;
import com.trendyol.typroductinformationman.model.Category;
import com.trendyol.typroductinformationman.model.Product;
import com.trendyol.typroductinformationman.model.ProductStock;
import com.trendyol.typroductinformationman.repository.CategoryRepository;
import com.trendyol.typroductinformationman.repository.ProductRepository;
import com.trendyol.typroductinformationman.repository.ProductStockRepository;
import com.trendyol.typroductinformationman.service.ProductService;
import com.trendyol.typroductinformationman.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductStockRepository productStockRepository;
    private CategoryRepository categoryRepository;
    private KafkaTemplate<String, Object> kafkaTemplate;
    private UserService userService;
    private Environment environment;
    private RestTemplate restTemplate;

    private static final String USER_NOTIFICATION_MESSAGE = "UserNotificationMessage";

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ProductStockRepository productStockRepository,
                              CategoryRepository categoryRepository,
                              KafkaTemplate<String,Object> kafkaTemplate,
                              UserService userService,
                              Environment environment,
                              RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.productStockRepository = productStockRepository;
        this.categoryRepository = categoryRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.userService = userService;
        this.environment = environment;
        this.restTemplate = restTemplate;
    }

    @Override
    public ProductDto getProduct(Integer productId) {
        Product product = productRepository.findById(productId.longValue()).orElseThrow(EntityNotFoundException::new);

        ProductStock productStock = productStockRepository.findByProduct_Id(productId.longValue());

        ProductDto productDto = new ProductDto();
        productDto.setBarcode(product.getBarcode());
        productDto.setImageURL(product.getImageURL());
        productDto.setPrice(product.getPrice());
        productDto.setTitle(product.getTitle());
        productDto.setCategoryId(product.getCategory().getId().intValue());
        productDto.setProductId(productId);

        productDto.setStock(productStock.getStockQuantity());

        return productDto;
    }

    @Override
    public String createProduct(CreateProductDto productDto) {
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setImageURL(productDto.getImageURL());
        product.setBarcode(productDto.getBarcode());

        Category category = categoryRepository.findById(Long.valueOf(productDto.getCategoryId())).orElseThrow(EntityNotFoundException :: new);
        product.setCategory(category);
        productRepository.save(product);

        ProductStock productStock = new ProductStock();
        productStock.setProduct(product);
        productStock.setStockQuantity(productDto.getStock());
        productStockRepository.save(productStock);

        return "product created";
    }

    @Override
    public String updateProduct(UpdateProductDto updateProductDto) {
        Product product = productRepository.findById(updateProductDto.getProductId()).orElseThrow(EntityNotFoundException::new);

        if(!Objects.isNull(updateProductDto.getImageURL()))
            product.setImageURL(updateProductDto.getImageURL());

        if(!Objects.isNull(updateProductDto.getTitle()))
            product.setTitle(updateProductDto.getTitle());

        if(!Objects.isNull(updateProductDto.getBarcode()))
            product.setBarcode(updateProductDto.getBarcode());

        if(!Objects.isNull(updateProductDto.getPrice()) && !updateProductDto.getPrice().equals(product.getPrice())) {
            if(updateProductDto.getPrice() < product.getPrice()) {
                 List<Integer> userIdList = getUser(updateProductDto.getProductId().intValue());

                 if(!userIdList.isEmpty()) {
                     List<UserDto> userInformationList = userService.getUserList(userIdList);
                     UserMessageNotificationDto userMessageNotificationDto = new UserMessageNotificationDto();
                     userMessageNotificationDto.setUserList(userInformationList);
                     userMessageNotificationDto.setUserNotificationType(UserNotificationType.PRICECHANGED);
                     kafkaTemplate.send(USER_NOTIFICATION_MESSAGE,userMessageNotificationDto);
                 }

            }
            product.setPrice(updateProductDto.getPrice());
        }

        productRepository.save(product);

        ProductStock productStock = productStockRepository.findByProduct_Id(updateProductDto.getProductId().longValue());
        if(Objects.nonNull(updateProductDto.getStock()) && !updateProductDto.getStock().equals(productStock.getStockQuantity())) {
            productStock.setStockQuantity(updateProductDto.getStock());

            productStockRepository.save(productStock);

            if(productStock.getStockQuantity() != 0 && productStock.getStockQuantity() < 3) {
                List<Integer> userIdList = getUser(updateProductDto.getProductId().intValue());
                List<UserDto> userInformationList = userService.getUserList(userIdList);
                UserMessageNotificationDto userMessageNotificationDto = new UserMessageNotificationDto();
                userMessageNotificationDto.setUserList(userInformationList);
                userMessageNotificationDto.setUserNotificationType(UserNotificationType.LESSTHANTHREE);

                kafkaTemplate.send(USER_NOTIFICATION_MESSAGE,userMessageNotificationDto);

            } else if(productStock.getStockQuantity() == 0) {
                List<Integer> userIdList = getUser(updateProductDto.getProductId().intValue());
                List<UserDto> userInformationList = userService.getUserList(userIdList);
                UserMessageNotificationDto userMessageNotificationDto = new UserMessageNotificationDto();
                userMessageNotificationDto.setUserList(userInformationList);
                userMessageNotificationDto.setUserNotificationType(UserNotificationType.OUTOFSTOCK);

                kafkaTemplate.send(USER_NOTIFICATION_MESSAGE,userMessageNotificationDto);
            }
        }


        return "product updated";
    }

    @Override
    public String updateProductStock(ProductStockDto productStockDto) {
        ProductStock productStock = productStockRepository.findByProduct_Id(productStockDto.getProductId().longValue());

        if(Objects.nonNull(productStockDto.getStockQuantity()) && !productStockDto.getStockQuantity().equals(productStock.getStockQuantity())) {
            productStock.setStockQuantity(productStockDto.getStockQuantity());
        }

        productStockRepository.save(productStock);

        return "product stock updated";
    }

    @Override
    public String addCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());

        categoryRepository.save(category);

        return "category added";
    }

    private List<Integer> getUser(Integer productId) {

        String url = environment.getProperty("cart.fetchUsersWithProductsInCart");

        String result = restTemplate.getForObject(url + productId,String.class);

        ObjectMapper jsonMapper = new ObjectMapper();

        List<Integer> userIdList = new ArrayList<>();

        try {
            userIdList = jsonMapper.readValue(result, new TypeReference<List<Integer>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userIdList;
    }
}
