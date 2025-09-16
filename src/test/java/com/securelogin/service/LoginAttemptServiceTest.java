package com.securelogin.service;

import com.securelogin.entity.LoginAttempt;
import com.securelogin.repository.LoginAttemptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {

    @Mock
    private LoginAttemptRepository loginAttemptRepository;

    @InjectMocks
    private LoginAttemptService loginAttemptService;

    private LoginAttempt successfulAttempt;
    private LoginAttempt failedAttempt;

    @BeforeEach
    void setUp() {
        successfulAttempt = new LoginAttempt();
        successfulAttempt.setId("1");
        successfulAttempt.setUsername("testuser");
        successfulAttempt.setIpAddress("192.168.1.1");
        successfulAttempt.setSuccess(true);
        successfulAttempt.setAttemptTime(LocalDateTime.now());
        successfulAttempt.setUserAgent("Mozilla/5.0");

        failedAttempt = new LoginAttempt();
        failedAttempt.setId("2");
        failedAttempt.setUsername("testuser");
        failedAttempt.setIpAddress("192.168.1.1");
        failedAttempt.setSuccess(false);
        failedAttempt.setAttemptTime(LocalDateTime.now());
        failedAttempt.setUserAgent("Mozilla/5.0");
        failedAttempt.setFailureReason("Invalid credentials");
    }

    @Test
    void recordLoginAttempt_ShouldSaveAttempt_WhenValidData() {
        when(loginAttemptRepository.save(any(LoginAttempt.class))).thenReturn(successfulAttempt);

        loginAttemptService.recordLoginAttempt("testuser", "192.168.1.1", true, "Mozilla/5.0");

        verify(loginAttemptRepository).save(any(LoginAttempt.class));
    }

    @Test
    void recordLoginAttempt_ShouldSaveAttemptWithFailureReason_WhenProvided() {
        when(loginAttemptRepository.save(any(LoginAttempt.class))).thenReturn(failedAttempt);

        loginAttemptService.recordLoginAttempt("testuser", "192.168.1.1", false, "Mozilla/5.0", "Invalid credentials");

        verify(loginAttemptRepository).save(any(LoginAttempt.class));
    }

    @Test
    void getSuccessfulLoginAttemptsByUsername_ShouldReturnSuccessfulAttempts() {
        List<LoginAttempt> attempts = Arrays.asList(successfulAttempt);
        when(loginAttemptRepository.findByUsernameAndSuccessOrderByAttemptTimeDesc("testuser", true))
            .thenReturn(attempts);

        List<LoginAttempt> result = loginAttemptService.getSuccessfulLoginAttemptsByUsername("testuser");

        assertEquals(1, result.size());
        assertTrue(result.get(0).isSuccess());
        verify(loginAttemptRepository).findByUsernameAndSuccessOrderByAttemptTimeDesc("testuser", true);
    }

    @Test
    void getFailedLoginAttemptsByUsername_ShouldReturnFailedAttempts() {
        List<LoginAttempt> attempts = Arrays.asList(failedAttempt);
        when(loginAttemptRepository.findByUsernameAndSuccessOrderByAttemptTimeDesc("testuser", false))
            .thenReturn(attempts);

        List<LoginAttempt> result = loginAttemptService.getFailedLoginAttemptsByUsername("testuser");

        assertEquals(1, result.size());
        assertFalse(result.get(0).isSuccess());
        verify(loginAttemptRepository).findByUsernameAndSuccessOrderByAttemptTimeDesc("testuser", false);
    }

    @Test
    void getRecentFailedAttemptsByIp_ShouldReturnFailedAttemptsFromLastHours() {
        List<LoginAttempt> attempts = Arrays.asList(failedAttempt);
        when(loginAttemptRepository.findFailedAttemptsByIpSince(anyString(), any(LocalDateTime.class)))
            .thenReturn(attempts);

        List<LoginAttempt> result = loginAttemptService.getRecentFailedAttemptsByIp("192.168.1.1", 24);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isSuccess());
        verify(loginAttemptRepository).findFailedAttemptsByIpSince(eq("192.168.1.1"), any(LocalDateTime.class));
    }

    @Test
    void generateSecurityReport_ShouldReturnCorrectReport() {
        List<LoginAttempt> allAttempts = Arrays.asList(successfulAttempt, failedAttempt);
        List<LoginAttempt> failedAttempts = Arrays.asList(failedAttempt);

        when(loginAttemptRepository.findRecentAttempts(any(LocalDateTime.class))).thenReturn(allAttempts);
        when(loginAttemptRepository.findFailedAttemptsSince(any(LocalDateTime.class))).thenReturn(failedAttempts);

        LoginAttemptService.SecurityReport report = loginAttemptService.generateSecurityReport();

        assertNotNull(report);
        assertEquals(2, report.getTotalAttempts());
        assertEquals(1, report.getSuccessfulAttempts());
        assertEquals(1, report.getFailedAttempts());
        assertEquals(50.0, report.getSuccessRate(), 0.1);
        assertEquals(50.0, report.getFailureRate(), 0.1);
    }

    @Test
    void generateSecurityReport_ShouldHandleEmptyAttempts() {
        when(loginAttemptRepository.findRecentAttempts(any(LocalDateTime.class))).thenReturn(Arrays.asList());
        when(loginAttemptRepository.findFailedAttemptsSince(any(LocalDateTime.class))).thenReturn(Arrays.asList());

        LoginAttemptService.SecurityReport report = loginAttemptService.generateSecurityReport();

        assertNotNull(report);
        assertEquals(0, report.getTotalAttempts());
        assertEquals(0, report.getSuccessfulAttempts());
        assertEquals(0, report.getFailedAttempts());
        assertEquals(0.0, report.getSuccessRate());
        assertEquals(0.0, report.getFailureRate());
    }

    @Test
    void securityReport_ShouldCalculateRatesCorrectly() {
        LoginAttemptService.SecurityReport report = new LoginAttemptService.SecurityReport(10, 7, 3, LocalDateTime.now());

        assertEquals(70.0, report.getSuccessRate(), 0.1);
        assertEquals(30.0, report.getFailureRate(), 0.1);
    }
}

