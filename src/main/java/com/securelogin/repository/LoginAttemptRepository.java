package com.securelogin.repository;

import com.securelogin.entity.LoginAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginAttemptRepository extends MongoRepository<LoginAttempt, String> {
    
    List<LoginAttempt> findByUsernameOrderByAttemptTimeDesc(String username);
    
    List<LoginAttempt> findByUsernameAndSuccessOrderByAttemptTimeDesc(String username, boolean success);
    
    @Query("{ 'attemptTime': { $gte: ?0 } }")
    List<LoginAttempt> findRecentAttempts(LocalDateTime since);
    
    @Query("{ 'success': false, 'attemptTime': { $gte: ?0 } }")
    List<LoginAttempt> findFailedAttemptsSince(LocalDateTime since);
    
    @Query("{ 'ipAddress': ?0, 'success': false, 'attemptTime': { $gte: ?1 } }")
    List<LoginAttempt> findFailedAttemptsByIpSince(String ipAddress, LocalDateTime since);
}

