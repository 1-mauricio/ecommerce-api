package com.ecommerce.service;

import com.ecommerce.dto.report.MonthlyRevenueDTO;
import com.ecommerce.dto.report.TopUserDTO;
import com.ecommerce.dto.report.UserAverageTicketDTO;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    public List<TopUserDTO> getTop5UsersByPurchases() {
        List<Object[]> results = userRepository.findTop5UsersByTotalSpent();
        
        return results.stream()
            .map(result -> {
                TopUserDTO dto = new TopUserDTO();
                dto.setUserId(UUID.fromString(result[0].toString())); 
                dto.setEmail(result[1].toString());
                dto.setName(result[2].toString()); 
                dto.setOrderCount(((Number) result[3]).longValue()); 
                dto.setTotalSpent((BigDecimal) result[4]); 
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    public List<UserAverageTicketDTO> getUserAverageTickets() {
        List<Object[]> results = orderRepository.findAverageTicketByUser();
        
        return results.stream()
            .map(result -> {
                UserAverageTicketDTO dto = new UserAverageTicketDTO();
                dto.setEmail(result[0].toString());
                dto.setName(result[1].toString());
                dto.setAverageTicket(BigDecimal.valueOf(((Number) result[2]).doubleValue()));
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    public MonthlyRevenueDTO getMonthlyRevenue(int year, int month) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);
            
            BigDecimal totalRevenue = orderRepository.findTotalRevenueByPeriod(startDate, endDate);
            Long orderCount = orderRepository.countPaidOrdersByPeriod(startDate, endDate);
            
            if (totalRevenue == null) {
                totalRevenue = BigDecimal.ZERO;
            }
            if (orderCount == null) {
                orderCount = 0L;
            }
            
            MonthlyRevenueDTO dto = new MonthlyRevenueDTO();
            dto.setYear(year);
            dto.setMonth(month);
            dto.setTotalRevenue(totalRevenue);
            dto.setOrderCount(orderCount);
            
            return dto;
        } catch (Exception e) {
            System.err.println("Erro no getMonthlyRevenue: " + e.getMessage());
            throw e;
        }
    }
}
