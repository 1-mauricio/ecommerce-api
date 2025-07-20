package com.ecommerce.dto.report;

import java.math.BigDecimal;
import java.util.UUID;

public class TopUserDTO {
    private UUID userId;
    private String email;
    private String name;
    private Long orderCount;
    private BigDecimal totalSpent;
    
    public TopUserDTO() {
    }
    
    public TopUserDTO(UUID userId, String email, String name, Long orderCount, BigDecimal totalSpent) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.orderCount = orderCount;
        this.totalSpent = totalSpent;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Long getOrderCount() {
        return orderCount;
    }
    
    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }
    
    public BigDecimal getTotalSpent() {
        return totalSpent;
    }
    
    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }
}
