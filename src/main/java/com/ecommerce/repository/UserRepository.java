package com.ecommerce.repository;

import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    @Query(value = """
        SELECT u.id, u.email, u.name,
               COUNT(CASE WHEN o.status = 'PAID' THEN o.id END) as order_count,
               COALESCE(SUM(CASE WHEN o.status = 'PAID' THEN o.total_amount ELSE 0 END), 0) as total_spent
        FROM users u
        LEFT JOIN orders o ON u.id = o.user_id
        WHERE u.role = 'USER'
        GROUP BY u.id, u.email, u.name
        ORDER BY total_spent DESC, u.email
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findTop5UsersByTotalSpent();
}
