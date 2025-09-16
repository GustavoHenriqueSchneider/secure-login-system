package com.securelogin.repository;

import com.securelogin.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByIsActiveTrue();
    
    @Query("{ 'roles': { $in: [?0] } }")
    List<User> findByRole(String role);
    
    @Query("{ 'lockedUntil': { $gt: ?0 } }")
    List<User> findLockedUsers(LocalDateTime now);
    
    
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
