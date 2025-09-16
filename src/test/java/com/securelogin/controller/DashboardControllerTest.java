package com.securelogin.controller;

import com.securelogin.entity.LoginAttempt;
import com.securelogin.entity.User;
import com.securelogin.service.LoginAttemptService;
import com.securelogin.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private LoginAttemptService loginAttemptService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DashboardController dashboardController;

    private User testUser;
    private List<LoginAttempt> recentLogins;
    private LoginAttemptService.SecurityReport securityReport;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("1");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setFullName("Test User");
        testUser.setRoles(new HashSet<>(Arrays.asList("USER")));

        LoginAttempt login1 = new LoginAttempt();
        login1.setId("1");
        login1.setUsername("testuser");
        login1.setSuccess(true);
        login1.setAttemptTime(LocalDateTime.now().minusHours(1));

        LoginAttempt login2 = new LoginAttempt();
        login2.setId("2");
        login2.setUsername("testuser");
        login2.setSuccess(true);
        login2.setAttemptTime(LocalDateTime.now().minusHours(2));

        recentLogins = Arrays.asList(login1, login2);

        securityReport = new LoginAttemptService.SecurityReport(10, 8, 2, LocalDateTime.now().minusHours(24));
    }

    @Test
    void dashboard_ShouldReturnDashboardPage_WhenUserExists() {
        when(authentication.getName()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(loginAttemptService.getSuccessfulLoginAttemptsByUsername("testuser")).thenReturn(recentLogins);
        when(loginAttemptService.generateSecurityReport()).thenReturn(securityReport);

        String result = dashboardController.dashboard(model, authentication);

        assertEquals("dashboard/index", result);
        verify(model).addAttribute("user", testUser);
        verify(model).addAttribute("recentLogins", recentLogins.subList(0, 2));
        verify(model).addAttribute("securityReport", securityReport);
    }

    @Test
    void dashboard_ShouldThrowException_WhenUserNotFound() {
        when(authentication.getName()).thenReturn("nonexistent");
        when(userService.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            dashboardController.dashboard(model, authentication));
    }

    @Test
    void dashboard_ShouldLimitRecentLoginsToFive() {
        List<LoginAttempt> manyLogins = Arrays.asList(
            createLoginAttempt("1", 1),
            createLoginAttempt("2", 2),
            createLoginAttempt("3", 3),
            createLoginAttempt("4", 4),
            createLoginAttempt("5", 5),
            createLoginAttempt("6", 6),
            createLoginAttempt("7", 7)
        );

        when(authentication.getName()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(loginAttemptService.getSuccessfulLoginAttemptsByUsername("testuser")).thenReturn(manyLogins);
        when(loginAttemptService.generateSecurityReport()).thenReturn(securityReport);

        String result = dashboardController.dashboard(model, authentication);

        assertEquals("dashboard/index", result);
        verify(model).addAttribute(eq("recentLogins"), argThat(logins -> 
            ((List<?>) logins).size() == 5));
    }

    @Test
    void profile_ShouldReturnProfilePage_WhenUserExists() {
        when(authentication.getName()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        String result = dashboardController.profile(model, authentication);

        assertEquals("dashboard/profile", result);
        verify(model).addAttribute("user", testUser);
    }

    @Test
    void profile_ShouldThrowException_WhenUserNotFound() {
        when(authentication.getName()).thenReturn("nonexistent");
        when(userService.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            dashboardController.profile(model, authentication));
    }

    private LoginAttempt createLoginAttempt(String id, int hoursAgo) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setId(id);
        attempt.setUsername("testuser");
        attempt.setSuccess(true);
        attempt.setAttemptTime(LocalDateTime.now().minusHours(hoursAgo));
        return attempt;
    }
}
