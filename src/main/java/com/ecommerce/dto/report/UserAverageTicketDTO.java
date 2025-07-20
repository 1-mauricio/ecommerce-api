package com.ecommerce.dto.report;

import java.math.BigDecimal;

public class UserAverageTicketDTO {
    private String email;
    private String name;
    private BigDecimal averageTicket;
    
    public UserAverageTicketDTO() {
    }
    
    public UserAverageTicketDTO(String email, String name, BigDecimal averageTicket) {
        this.email = email;
        this.name = name;
        this.averageTicket = averageTicket;
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
    
    public BigDecimal getAverageTicket() {
        return averageTicket;
    }
    
    public void setAverageTicket(BigDecimal averageTicket) {
        this.averageTicket = averageTicket;
    }
}
