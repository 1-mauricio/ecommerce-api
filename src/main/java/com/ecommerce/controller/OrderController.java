package com.ecommerce.controller;

import com.ecommerce.dto.order.OrderRequestDTO;
import com.ecommerce.dto.order.OrderResponseDTO;
import com.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('USER')")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        OrderResponseDTO order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @PostMapping("/{id}/pay")
    public ResponseEntity<OrderResponseDTO> payOrder(@PathVariable UUID id) {
        OrderResponseDTO order = orderService.payOrder(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable UUID id) {
        OrderResponseDTO order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getUserOrders(Pageable pageable) {
        Page<OrderResponseDTO> orders = orderService.getUserOrders(pageable);
        return ResponseEntity.ok(orders);
    }
}
