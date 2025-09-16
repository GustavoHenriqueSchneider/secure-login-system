package com.securelogin.controller;

import com.securelogin.dto.UserRegistrationDto;
import com.securelogin.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @InjectMocks
    private AuthController authController;

    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setUsername("testuser");
        userRegistrationDto.setEmail("test@example.com");
        userRegistrationDto.setPassword("password");
        userRegistrationDto.setConfirmPassword("password");
        userRegistrationDto.setFullName("Test User");
    }

    @Test
    void loginPage_ShouldReturnLoginView() {
        String result = authController.loginPage(model, null, null, null);

        assertEquals("auth/login", result);
    }

    @Test
    void registerPage_ShouldReturnRegisterView() {
        String result = authController.registerPage(model);

        assertEquals("auth/register", result);
        verify(model).addAttribute(eq("userRegistrationDto"), any(UserRegistrationDto.class));
    }

    @Test
    void registerUser_ShouldReturnRegisterView_WhenValidationErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = authController.registerUser(userRegistrationDto, bindingResult, redirectAttributes);

        assertEquals("auth/register", result);
    }

    @Test
    void registerUser_ShouldReturnRegisterView_WhenPasswordsDoNotMatch() {
        when(bindingResult.hasErrors()).thenReturn(false);
        userRegistrationDto.setConfirmPassword("different");

        String result = authController.registerUser(userRegistrationDto, bindingResult, redirectAttributes);

        assertEquals("auth/register", result);
        verify(bindingResult).rejectValue(eq("confirmPassword"), anyString(), anyString());
    }

    @Test
    void registerUser_ShouldRedirectToLogin_WhenSuccessful() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.createUser(any())).thenReturn(new com.securelogin.entity.User());

        String result = authController.registerUser(userRegistrationDto, bindingResult, redirectAttributes);

        assertEquals("redirect:/login", result);
        verify(redirectAttributes).addFlashAttribute(eq("successMessage"), anyString());
    }

    @Test
    void accessDeniedPage_ShouldReturnAccessDeniedView() {
        String result = authController.accessDeniedPage(model);

        assertEquals("error/access-denied", result);
    }
}
