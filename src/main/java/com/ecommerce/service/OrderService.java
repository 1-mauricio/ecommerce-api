package com.ecommerce.service;

import com.ecommerce.dto.order.OrderRequestDTO;
import com.ecommerce.dto.order.OrderResponseDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Validar produtos e estoque
        List<UUID> productIds = request.getItems().stream()
            .map(OrderRequestDTO.OrderItemRequestDTO::getProductId)
            .collect(Collectors.toList());
        
        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.size() != productIds.size()) {
            throw new RuntimeException("One or more products not found");
        }
        
        Map<UUID, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getId, p -> p));
        
        // Verificar estoque
        for (OrderRequestDTO.OrderItemRequestDTO item : request.getItems()) {
            Product product = productMap.get(item.getProductId());
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
        }
        
        // Criar pedido
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (OrderRequestDTO.OrderItemRequestDTO itemRequest : request.getItems()) {
            Product product = productMap.get(itemRequest.getProductId());
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }
        
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        
        order = orderRepository.save(order);
        
        return mapToResponseDTO(order);
    }
    
    public OrderResponseDTO payOrder(UUID orderId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Order does not belong to current user");
        }
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order is not pending");
        }
        
        // Verificar estoque novamente
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            if (product.getStockQuantity() < item.getQuantity()) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                throw new RuntimeException("Insufficient stock for product: " + product.getName() + ". Order cancelled.");
            }
        }
        
        // Atualizar estoque
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }
        
        order.setStatus(OrderStatus.PAID);
        order = orderRepository.save(order);
        
        return mapToResponseDTO(order);
    }
    
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrder(UUID orderId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Order does not belong to current user");
        }
        
        return mapToResponseDTO(order);
    }
    
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getUserOrders(Pageable pageable) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return orderRepository.findByUserId(user.getId(), pageable)
            .map(this::mapToResponseDTO);
    }
    
    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        List<OrderResponseDTO.OrderItemResponseDTO> itemDTOs = order.getItems().stream()
            .map(item -> {
                OrderResponseDTO.OrderItemResponseDTO itemDTO = new OrderResponseDTO.OrderItemResponseDTO();
                itemDTO.setId(item.getId());
                itemDTO.setProductId(item.getProduct().getId());
                itemDTO.setProductName(item.getProduct().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setUnitPrice(item.getUnitPrice());
                itemDTO.setTotalPrice(item.getTotalPrice());
                return itemDTO;
            })
            .collect(Collectors.toList());
        
        dto.setItems(itemDTOs);
        return dto;
    }
}
