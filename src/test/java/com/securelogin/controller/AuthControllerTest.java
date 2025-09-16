package com.securelogin.controller;

import com.securelogin.dto.UserRegistrationDto;
import com.securelogin.entity.User;
import com.securelogin.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthController authController;

    private UserRegistrationDto validDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        validDto = new UserRegistrationDto();
        validDto.setUsername("testuser");
        validDto.setEmail("test@example.com");
        validDto.setFullName("Test User");
        validDto.setPassword("password123");
        validDto.setConfirmPassword("password123");

        testUser = new User();
        testUser.setId("1");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setFullName("Test User");
        testUser.setRoles(new HashSet<>(Arrays.asList("USER")));
    }

    @Test
    void loginPage_ShouldReturnLoginPage_WhenNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.setContext(securityContext);

        String result = authController.loginPage(model, null, null, null);

        assertEquals("auth/login", result);
    }

    @Test
    void loginPage_ShouldRedirectToDashboard_WhenAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        String result = authController.loginPage(model, null, null, null);

        assertEquals("redirect:/dashboard", result);
    }

    @Test
    void loginPage_ShouldAddErrorMessage_WhenErrorParameter() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.setContext(securityContext);

        String result = authController.loginPage(model, "invalid_credentials", null, null);

        assertEquals("auth/login", result);
        verify(model).addAttribute("errorMessage", "Nome de usuário ou senha inválidos.");
    }

    @Test
    void loginPage_ShouldAddSuccessMessage_WhenLogoutParameter() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.setContext(securityContext);

        String result = authController.loginPage(model, null, "true", null);

        assertEquals("auth/login", result);
        verify(model).addAttribute("successMessage", "Logout realizado com sucesso!");
    }

    @Test
    void loginPage_ShouldAddExpiredMessage_WhenExpiredParameter() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.setContext(securityContext);

        String result = authController.loginPage(model, null, null, "true");

        assertEquals("auth/login", result);
        verify(model).addAttribute("errorMessage", "Sua sessão expirou. Faça login novamente.");
    }

    @Test
    void registerPage_ShouldReturnRegisterPage_WhenNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.setContext(securityContext);

        String result = authController.registerPage(model);

        assertEquals("auth/register", result);
        verify(model).addAttribute(eq("userRegistrationDto"), any(UserRegistrationDto.class));
    }

    @Test
    void registerPage_ShouldRedirectToDashboard_WhenAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        String result = authController.registerPage(model);

        assertEquals("redirect:/dashboard", result);
    }

    @Test
    void registerUser_ShouldReturnRegisterPage_WhenValidationErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = authController.registerUser(validDto, bindingResult, redirectAttributes);

        assertEquals("auth/register", result);
        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    void registerUser_ShouldReturnRegisterPage_WhenPasswordsDoNotMatch() {
        validDto.setConfirmPassword("different_password");
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = authController.registerUser(validDto, bindingResult, redirectAttributes);

        assertEquals("auth/register", result);
        verify(bindingResult).rejectValue("confirmPassword", "error.userRegistrationDto", "As senhas não coincidem");
        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    void registerUser_ShouldRedirectToLogin_WhenRegistrationSuccessful() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        String result = authController.registerUser(validDto, bindingResult, redirectAttributes);

        assertEquals("redirect:/login", result);
        verify(userService).createUser(any(User.class));
        verify(redirectAttributes).addFlashAttribute("successMessage", 
            "Usuário cadastrado com sucesso! Faça login para continuar.");
    }

    @Test
    void registerUser_ShouldReturnRegisterPage_WhenUsernameExists() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.createUser(any(User.class)))
            .thenThrow(new IllegalArgumentException("Nome de usuário já existe: testuser"));

        String result = authController.registerUser(validDto, bindingResult, redirectAttributes);

        assertEquals("auth/register", result);
        verify(bindingResult).rejectValue("username", "error.userRegistrationDto", 
            "Nome de usuário já existe: testuser");
    }

    @Test
    void registerUser_ShouldRedirectToRegister_WhenUnexpectedError() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.createUser(any(User.class)))
            .thenThrow(new RuntimeException("Database error"));

        String result = authController.registerUser(validDto, bindingResult, redirectAttributes);

        assertEquals("redirect:/register", result);
        verify(redirectAttributes).addFlashAttribute("errorMessage", 
            "Erro interno do servidor. Tente novamente mais tarde.");
    }

    @Test
    void accessDeniedPage_ShouldReturnAccessDeniedPage() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        String result = authController.accessDeniedPage(model);

        assertEquals("error/access-denied", result);
        verify(model).addAttribute("username", "testuser");
    }

    @Test
    void accessDeniedPage_ShouldReturnAccessDeniedPage_WhenNoAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        String result = authController.accessDeniedPage(model);

        assertEquals("error/access-denied", result);
        verify(model, never()).addAttribute(eq("username"), anyString());
    }
}