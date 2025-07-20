package com.ecommerce.service;

import com.ecommerce.dto.order.OrderRequestDTO;
import com.ecommerce.dto.order.OrderResponseDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.enums.UserRole;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;
    private Order order;
    private OrderRequestDTO orderRequestDTO;
    private UUID userId;
    private UUID productId;
    private UUID orderId;

    @BeforeEach
    void setUp() {

        userId = UUID.randomUUID();
        productId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setRole(UserRole.USER);
        
        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("99.99"));
        product.setStockQuantity(10);

        order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("99.99"));
        
        // Criar OrderItem para o pedido
        OrderItem orderItem = new OrderItem();
        orderItem.setId(UUID.randomUUID());
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(1);
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setTotalPrice(product.getPrice());
        
        order.setItems(List.of(orderItem));

        orderRequestDTO = new OrderRequestDTO();
        OrderRequestDTO.OrderItemRequestDTO itemRequest = new OrderRequestDTO.OrderItemRequestDTO();
        itemRequest.setProductId(productId);
        itemRequest.setQuantity(1);
        orderRequestDTO.setItems(List.of(itemRequest));

        // Setup SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createOrder_ShouldCreateOrderSuccessfully() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findByIdIn(List.of(productId))).thenReturn(List.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        OrderResponseDTO result = orderService.createOrder(orderRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(new BigDecimal("99.99"), result.getTotalAmount());
        verify(userRepository).findByEmail("test@example.com");
        verify(productRepository).findByIdIn(List.of(productId));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderRequestDTO));
        verify(userRepository).findByEmail("test@example.com");
        verify(productRepository, never()).findByIdIn(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_WhenProductNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findByIdIn(List.of(productId))).thenReturn(List.of());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderRequestDTO));
        verify(userRepository).findByEmail("test@example.com");
        verify(productRepository).findByIdIn(List.of(productId));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_WhenInsufficientStock_ShouldThrowException() {
        // Arrange
        product.setStockQuantity(0);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findByIdIn(List.of(productId))).thenReturn(List.of(product));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderRequestDTO));
        verify(userRepository).findByEmail("test@example.com");
        verify(productRepository).findByIdIn(List.of(productId));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void payOrder_ShouldPayOrderSuccessfully() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        OrderResponseDTO result = orderService.payOrder(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.PAID, result.getStatus());
        verify(userRepository).findByEmail("test@example.com");
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void payOrder_WhenOrderNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.payOrder(orderId));
        verify(userRepository).findByEmail("test@example.com");
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void payOrder_WhenOrderDoesNotBelongToUser_ShouldThrowException() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        order.setUser(otherUser);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.payOrder(orderId));
        verify(userRepository).findByEmail("test@example.com");
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void payOrder_WhenOrderIsNotPending_ShouldThrowException() {
        // Arrange
        order.setStatus(OrderStatus.PAID);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.payOrder(orderId));
        verify(userRepository).findByEmail("test@example.com");
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrder_ShouldReturnOrder() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        OrderResponseDTO result = orderService.getOrder(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(userRepository).findByEmail("test@example.com");
        verify(orderRepository).findById(orderId);
    }

    @Test
    void getOrder_WhenOrderDoesNotBelongToUser_ShouldThrowException() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        order.setUser(otherUser);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.getOrder(orderId));
        verify(userRepository).findByEmail("test@example.com");
        verify(orderRepository).findById(orderId);
    }

    @Test
    void getUserOrders_ShouldReturnUserOrders() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findByUserId(userId, pageable)).thenReturn(orderPage);

        // Act
        Page<OrderResponseDTO> result = orderService.getUserOrders(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository).findByEmail("test@example.com");
        verify(orderRepository).findByUserId(userId, pageable);
    }
} 