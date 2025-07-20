package com.ecommerce.dto.report;

import java.math.BigDecimal;

public class MonthlyRevenueDTO {
    private Integer year;
    private Integer month;
    private BigDecimal totalRevenue;
    private Long orderCount;
    
    public MonthlyRevenueDTO() {
    }
    
    public MonthlyRevenueDTO(Integer year, Integer month, BigDecimal totalRevenue, Long orderCount) {
        this.year = year;
        this.month = month;
        this.totalRevenue = totalRevenue;
        this.orderCount = orderCount;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public Integer getMonth() {
        return month;
    }
    
    public void setMonth(Integer month) {
        this.month = month;
    }
    
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public Long getOrderCount() {
        return orderCount;
    }
    
    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }
}
