package com.ecommerce.controller;

import com.ecommerce.dto.report.MonthlyRevenueDTO;
import com.ecommerce.dto.report.TopUserDTO;
import com.ecommerce.dto.report.UserAverageTicketDTO;
import com.ecommerce.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    @GetMapping("/top-users")
    public ResponseEntity<List<TopUserDTO>> getTop5Users() {
        List<TopUserDTO> topUsers = reportService.getTop5UsersByPurchases();
        return ResponseEntity.ok(topUsers);
    }
    
    @GetMapping("/average-tickets")
    public ResponseEntity<List<UserAverageTicketDTO>> getUserAverageTickets() {
        List<UserAverageTicketDTO> averageTickets = reportService.getUserAverageTickets();
        return ResponseEntity.ok(averageTickets);
    }
    
    @GetMapping("/monthly-revenue")
    public ResponseEntity<MonthlyRevenueDTO> getMonthlyRevenue(@RequestParam int year, 
                                                              @RequestParam int month) {
        MonthlyRevenueDTO monthlyRevenue = reportService.getMonthlyRevenue(year, month);
        return ResponseEntity.ok(monthlyRevenue);
    }
}
