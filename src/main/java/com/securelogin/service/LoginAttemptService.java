package com.securelogin.service;

import com.securelogin.entity.LoginAttempt;
import com.securelogin.repository.LoginAttemptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    
    private final LoginAttemptRepository loginAttemptRepository;
    
    public void recordLoginAttempt(String username, String ipAddress, boolean success, String userAgent) {
        LoginAttempt attempt = new LoginAttempt(username, ipAddress, success, userAgent);
        loginAttemptRepository.save(attempt);
        log.debug("Login attempt recorded: {} - {}", username, success ? "SUCCESS" : "FAILED");
    }
    
    public void recordLoginAttempt(String username, String ipAddress, boolean success, String userAgent, String failureReason) {
        LoginAttempt attempt = new LoginAttempt(username, ipAddress, success, userAgent);
        attempt.setFailureReason(failureReason);
        loginAttemptRepository.save(attempt);
        log.debug("Login attempt recorded: {} - {} - {}", username, success ? "SUCCESS" : "FAILED", failureReason);
    }
    
    public List<LoginAttempt> getSuccessfulLoginAttemptsByUsername(String username) {
        return loginAttemptRepository.findByUsernameAndSuccessOrderByAttemptTimeDesc(username, true);
    }
    
    public List<LoginAttempt> getFailedLoginAttemptsByUsername(String username) {
        return loginAttemptRepository.findByUsernameAndSuccessOrderByAttemptTimeDesc(username, false);
    }
    
    public List<LoginAttempt> getRecentFailedAttemptsByIp(String ipAddress, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return loginAttemptRepository.findFailedAttemptsByIpSince(ipAddress, since);
    }
    
    public SecurityReport generateSecurityReport() {
        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        
        List<LoginAttempt> recentFailedAttempts = loginAttemptRepository.findFailedAttemptsSince(last24Hours);
        List<LoginAttempt> recentAttempts = loginAttemptRepository.findRecentAttempts(last24Hours);
        
        long totalAttempts = recentAttempts.size();
        long failedAttempts = recentFailedAttempts.size();
        long successfulAttempts = totalAttempts - failedAttempts;
        
        return new SecurityReport(totalAttempts, successfulAttempts, failedAttempts, last24Hours);
    }
    
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class SecurityReport {
        private long totalAttempts;
        private long successfulAttempts;
        private long failedAttempts;
        private LocalDateTime reportPeriod;
        
        public double getSuccessRate() {
            return totalAttempts > 0 ? (double) successfulAttempts / totalAttempts * 100 : 0;
        }
        
        public double getFailureRate() {
            return totalAttempts > 0 ? (double) failedAttempts / totalAttempts * 100 : 0;
        }
    }
}

