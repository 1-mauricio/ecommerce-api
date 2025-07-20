package com.ecommerce.repository;

import com.ecommerce.entity.Order;
import com.ecommerce.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    Page<Order> findByUserId(UUID userId, Pageable pageable);

    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);
    
    @Query("""
        SELECT u.email, u.name, AVG(o.totalAmount) as avg_ticket
        FROM Order o
        JOIN o.user u
        WHERE o.status = 'PAID'
        GROUP BY u.id, u.email, u.name
        ORDER BY avg_ticket DESC
        """)
    List<Object[]> findAverageTicketByUser();
    
    @Query("""
        SELECT CASE 
            WHEN SUM(o.totalAmount) IS NULL THEN 0 
            ELSE SUM(o.totalAmount) 
        END
        FROM Order o
        WHERE o.status = 'PAID'
        AND o.createdAt >= :startDate
        AND o.createdAt <= :endDate
        """)
    BigDecimal findTotalRevenueByPeriod(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    @Query("""
        SELECT COUNT(o.id)
        FROM Order o
        WHERE o.status = 'PAID'
        AND o.createdAt >= :startDate
        AND o.createdAt <= :endDate
        """)
    Long countPaidOrdersByPeriod(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);
}
