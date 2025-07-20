package com.ecommerce.service;

import com.ecommerce.dto.product.ProductRequestDTO;
import com.ecommerce.dto.product.ProductResponseDTO;
import com.ecommerce.dto.product.ProductUpdateDTO;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequestDTO productRequestDTO;
    private ProductUpdateDTO productUpdateDTO;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));
        product.setCategory("Electronics");
        product.setStockQuantity(10);

        productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName("New Product");
        productRequestDTO.setDescription("New Description");
        productRequestDTO.setPrice(new BigDecimal("149.99"));
        productRequestDTO.setCategory("Books");
        productRequestDTO.setStockQuantity(5);

        productUpdateDTO = new ProductUpdateDTO();
        productUpdateDTO.setName("Updated Product");
        productUpdateDTO.setPrice(new BigDecimal("199.99"));
        productUpdateDTO.setStockQuantity(15);
    }

    @Test
    void createProduct_ShouldReturnProductResponseDTO() {
        // Arrange
        Product savedProduct = new Product();
        savedProduct.setId(productId);
        savedProduct.setName(productRequestDTO.getName());
        savedProduct.setDescription(productRequestDTO.getDescription());
        savedProduct.setPrice(productRequestDTO.getPrice());
        savedProduct.setCategory(productRequestDTO.getCategory());
        savedProduct.setStockQuantity(productRequestDTO.getStockQuantity());
        savedProduct.setCreatedAt(LocalDateTime.now());
        savedProduct.setUpdatedAt(LocalDateTime.now());
        
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        ProductResponseDTO result = productService.createProduct(productRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(productRequestDTO.getName(), result.getName());
        assertEquals(productRequestDTO.getPrice(), result.getPrice());
        assertEquals(productRequestDTO.getCategory(), result.getCategory());
        assertEquals(productRequestDTO.getStockQuantity(), result.getStockQuantity());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProduct_WhenProductExists_ShouldReturnProduct() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        ProductResponseDTO result = productService.getProduct(productId);

        // Assert
        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());
        verify(productRepository).findById(productId);
    }

    @Test
    void getProduct_WhenProductDoesNotExist_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.getProduct(productId));
        verify(productRepository).findById(productId);
    }

    @Test
    void getAllProducts_ShouldReturnPageOfProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // Act
        Page<ProductResponseDTO> result = productService.getAllProducts(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(product.getName(), result.getContent().get(0).getName());
        verify(productRepository).findAll(pageable);
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductResponseDTO result = productService.updateProduct(productId, productUpdateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(productUpdateDTO.getName(), result.getName());
        assertEquals(productUpdateDTO.getPrice(), result.getPrice());
        assertEquals(productUpdateDTO.getStockQuantity(), result.getStockQuantity());
        verify(productRepository).findById(productId);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.updateProduct(productId, productUpdateDTO));
        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() {
        // Arrange
        when(productRepository.existsById(productId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(productId);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository).existsById(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldThrowException() {
        // Arrange
        when(productRepository.existsById(productId)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.deleteProduct(productId));
        verify(productRepository).existsById(productId);
        verify(productRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void searchProducts_ShouldReturnFilteredProducts() {
        // Arrange
        String searchTerm = "test";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findByNameContainingIgnoreCase(searchTerm, pageable)).thenReturn(productPage);

        // Act
        Page<ProductResponseDTO> result = productService.searchProducts(searchTerm, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository).findByNameContainingIgnoreCase(searchTerm, pageable);
    }

    @Test
    void getProductsByCategory_ShouldReturnCategoryProducts() {
        // Arrange
        String category = "Electronics";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findByCategory(category, pageable)).thenReturn(productPage);

        // Act
        Page<ProductResponseDTO> result = productService.getProductsByCategory(category, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository).findByCategory(category, pageable);
    }

    @Test
    void getCategories_ShouldReturnCategories() {
        // Arrange
        List<String> categories = List.of("Electronics", "Books", "Clothing");
        when(productRepository.findAllCategories()).thenReturn(categories);

        // Act
        List<String> result = productService.getCategories();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(categories));
        verify(productRepository).findAllCategories();
    }
} 