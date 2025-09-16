package com.securelogin.repository;

import com.securelogin.entity.LoginAttempt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/test-db"
})
class LoginAttemptRepositoryTest {

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    private LoginAttempt successfulAttempt;
    private LoginAttempt failedAttempt;

    @BeforeEach
    void setUp() {
        loginAttemptRepository.deleteAll();
        
        successfulAttempt = new LoginAttempt();
        successfulAttempt.setUsername("testuser");
        successfulAttempt.setIpAddress("192.168.1.1");
        successfulAttempt.setSuccess(true);
        successfulAttempt.setAttemptTime(LocalDateTime.now().minusHours(1));
        successfulAttempt.setUserAgent("Mozilla/5.0");

        failedAttempt = new LoginAttempt();
        failedAttempt.setUsername("testuser");
        failedAttempt.setIpAddress("192.168.1.1");
        failedAttempt.setSuccess(false);
        failedAttempt.setAttemptTime(LocalDateTime.now().minusHours(2));
        failedAttempt.setUserAgent("Mozilla/5.0");
        failedAttempt.setFailureReason("Invalid credentials");
    }

    @Test
    void findByUsernameOrderByAttemptTimeDesc_ShouldReturnAttemptsInDescendingOrder() {
        LoginAttempt olderAttempt = new LoginAttempt();
        olderAttempt.setUsername("testuser");
        olderAttempt.setIpAddress("192.168.1.1");
        olderAttempt.setSuccess(true);
        olderAttempt.setAttemptTime(LocalDateTime.now().minusHours(3));
        olderAttempt.setUserAgent("Mozilla/5.0");

        loginAttemptRepository.save(successfulAttempt);
        loginAttemptRepository.save(olderAttempt);

        List<LoginAttempt> result = loginAttemptRepository.findByUsernameOrderByAttemptTimeDesc("testuser");

        assertEquals(2, result.size());
        assertTrue(result.get(0).getAttemptTime().isAfter(result.get(1).getAttemptTime()));
    }

    @Test
    void findByUsernameAndSuccessOrderByAttemptTimeDesc_ShouldReturnSuccessfulAttempts() {
        loginAttemptRepository.save(successfulAttempt);
        loginAttemptRepository.save(failedAttempt);

        List<LoginAttempt> result = loginAttemptRepository.findByUsernameAndSuccessOrderByAttemptTimeDesc("testuser", true);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isSuccess());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    void findByUsernameAndSuccessOrderByAttemptTimeDesc_ShouldReturnFailedAttempts() {
        loginAttemptRepository.save(successfulAttempt);
        loginAttemptRepository.save(failedAttempt);

        List<LoginAttempt> result = loginAttemptRepository.findByUsernameAndSuccessOrderByAttemptTimeDesc("testuser", false);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isSuccess());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    void findRecentAttempts_ShouldReturnAttemptsSinceGivenTime() {
        LoginAttempt oldAttempt = new LoginAttempt();
        oldAttempt.setUsername("testuser");
        oldAttempt.setIpAddress("192.168.1.1");
        oldAttempt.setSuccess(true);
        oldAttempt.setAttemptTime(LocalDateTime.now().minusDays(2));
        oldAttempt.setUserAgent("Mozilla/5.0");

        loginAttemptRepository.save(successfulAttempt);
        loginAttemptRepository.save(oldAttempt);

        LocalDateTime since = LocalDateTime.now().minusHours(2);
        List<LoginAttempt> result = loginAttemptRepository.findRecentAttempts(since);

        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    void findFailedAttemptsSince_ShouldReturnFailedAttemptsSinceGivenTime() {
        loginAttemptRepository.save(successfulAttempt);
        loginAttemptRepository.save(failedAttempt);

        LocalDateTime since = LocalDateTime.now().minusHours(3);
        List<LoginAttempt> result = loginAttemptRepository.findFailedAttemptsSince(since);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isSuccess());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    void findFailedAttemptsByIpSince_ShouldReturnFailedAttemptsByIpSinceGivenTime() {
        LoginAttempt differentIpAttempt = new LoginAttempt();
        differentIpAttempt.setUsername("testuser");
        differentIpAttempt.setIpAddress("192.168.1.2");
        differentIpAttempt.setSuccess(false);
        differentIpAttempt.setAttemptTime(LocalDateTime.now().minusHours(1));
        differentIpAttempt.setUserAgent("Mozilla/5.0");

        loginAttemptRepository.save(failedAttempt);
        loginAttemptRepository.save(differentIpAttempt);

        LocalDateTime since = LocalDateTime.now().minusHours(3);
        List<LoginAttempt> result = loginAttemptRepository.findFailedAttemptsByIpSince("192.168.1.1", since);

        assertEquals(1, result.size());
        assertEquals("192.168.1.1", result.get(0).getIpAddress());
        assertFalse(result.get(0).isSuccess());
    }
}

