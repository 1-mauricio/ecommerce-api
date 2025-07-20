package com.ecommerce.service;

import com.ecommerce.dto.product.ProductRequestDTO;
import com.ecommerce.dto.product.ProductResponseDTO;
import com.ecommerce.dto.product.ProductUpdateDTO;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStockQuantity(request.getStockQuantity());
        
        product = productRepository.save(product);
        return mapToResponseDTO(product);
    }
    
    public ProductResponseDTO updateProduct(UUID id, ProductUpdateDTO request) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setName((request.getName() != null) ? request.getName() : product.getName());
        product.setDescription(request.getDescription() != null ? request.getDescription() : product.getDescription());
        product.setPrice(request.getPrice() != null ? request.getPrice() : product.getPrice());
        product.setCategory(request.getCategory() != null ? request.getCategory() : product.getCategory());
        product.setStockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : product.getStockQuantity());
        
        product = productRepository.save(product);
        return mapToResponseDTO(product);
    }
    
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public ProductResponseDTO getProduct(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponseDTO(product);
    }
    
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(this::mapToResponseDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProducts(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable)
            .map(this::mapToResponseDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable)
            .map(this::mapToResponseDTO);
    }
    
    @Transactional(readOnly = true)
    public List<String> getCategories() {
        return productRepository.findAllCategories();
    }
    
    private ProductResponseDTO mapToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
