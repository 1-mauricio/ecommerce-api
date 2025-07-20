package com.ecommerce.service;

import com.ecommerce.dto.report.MonthlyRevenueDTO;
import com.ecommerce.dto.report.TopUserDTO;
import com.ecommerce.dto.report.UserAverageTicketDTO;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void getTop5UsersByPurchases_ShouldNotThrowException() {
        // Arrange
        when(userRepository.findTop5UsersByTotalSpent()).thenReturn(List.of());

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<TopUserDTO> result = reportService.getTop5UsersByPurchases();
            assertNotNull(result);
        });
        
        verify(userRepository).findTop5UsersByTotalSpent();
    }

    @Test
    void getUserAverageTickets_ShouldNotThrowException() {
        // Arrange
        when(orderRepository.findAverageTicketByUser()).thenReturn(List.of());

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<UserAverageTicketDTO> result = reportService.getUserAverageTickets();
            assertNotNull(result);
        });
        
        verify(orderRepository).findAverageTicketByUser();
    }

    @Test
    void getMonthlyRevenue_ShouldNotThrowException() {
        // Arrange
        when(orderRepository.findTotalRevenueByPeriod(any(), any())).thenReturn(BigDecimal.ZERO);
        when(orderRepository.countPaidOrdersByPeriod(any(), any())).thenReturn(0L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            MonthlyRevenueDTO result = reportService.getMonthlyRevenue(2024, 7);
            assertNotNull(result);
            assertEquals(2024, result.getYear());
            assertEquals(7, result.getMonth());
        });
        
        verify(orderRepository).findTotalRevenueByPeriod(any(), any());
        verify(orderRepository).countPaidOrdersByPeriod(any(), any());
    }

    @Test
    void getMonthlyRevenue_WithNullValues_ShouldHandleGracefully() {
        // Arrange
        when(orderRepository.findTotalRevenueByPeriod(any(), any())).thenReturn(null);
        when(orderRepository.countPaidOrdersByPeriod(any(), any())).thenReturn(null);

        // Act & Assert
        assertDoesNotThrow(() -> {
            MonthlyRevenueDTO result = reportService.getMonthlyRevenue(2024, 12);
            assertNotNull(result);
            assertEquals(BigDecimal.ZERO, result.getTotalRevenue());
            assertEquals(0L, result.getOrderCount());
        });
    }

    @Test
    void getMonthlyRevenue_WithValidData_ShouldNotThrowException() {
        // Arrange
        when(orderRepository.findTotalRevenueByPeriod(any(), any())).thenReturn(BigDecimal.valueOf(1000.00));
        when(orderRepository.countPaidOrdersByPeriod(any(), any())).thenReturn(10L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            MonthlyRevenueDTO result = reportService.getMonthlyRevenue(2024, 12);
            assertNotNull(result);
            assertEquals(2024, result.getYear());
            assertEquals(12, result.getMonth());
            assertEquals(BigDecimal.valueOf(1000.00), result.getTotalRevenue());
            assertEquals(10L, result.getOrderCount());
        });
    }
} 