package com.securelogin.controller;

import com.securelogin.dto.UserRegistrationDto;
import com.securelogin.entity.User;
import com.securelogin.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @GetMapping("/login")
    public String loginPage(Model model, String error, String logout, String expired) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        
        if (error != null) {
            String errorMessage = getErrorMessage(error);
            model.addAttribute("errorMessage", errorMessage);
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "Logout realizado com sucesso!");
        }
        
        if (expired != null) {
            model.addAttribute("errorMessage", "Sua sessão expirou. Faça login novamente.");
        }
        
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid UserRegistrationDto userRegistrationDto, 
                              BindingResult bindingResult, 
                              RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            log.warn("Erro de validação no cadastro: {}", bindingResult.getAllErrors());
            return "auth/register";
        }
        
        if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.userRegistrationDto", "As senhas não coincidem");
            return "auth/register";
        }
        
        try {
            User user = new User();
            user.setUsername(userRegistrationDto.getUsername());
            user.setEmail(userRegistrationDto.getEmail());
            user.setPassword(userRegistrationDto.getPassword());
            user.setFullName(userRegistrationDto.getFullName());
            user.setRoles(new HashSet<>(Arrays.asList("USER")));
            
            User savedUser = userService.createUser(user);
            
            log.info("Usuário cadastrado com sucesso: {}", savedUser.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", 
                "Usuário cadastrado com sucesso! Faça login para continuar.");
            
            return "redirect:/login";
            
        } catch (IllegalArgumentException e) {
            log.warn("Erro no cadastro: {}", e.getMessage());
            bindingResult.rejectValue("username", "error.userRegistrationDto", e.getMessage());
            return "auth/register";
        } catch (Exception e) {
            log.error("Erro inesperado no cadastro: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erro interno do servidor. Tente novamente mais tarde.");
            return "redirect:/register";
        }
    }
    
    @GetMapping("/access-denied")
    public String accessDeniedPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            model.addAttribute("username", auth.getName());
        }
        return "error/access-denied";
    }
    
    private String getErrorMessage(String error) {
        switch (error) {
            case "invalid_credentials":
                return "Nome de usuário ou senha inválidos.";
            case "user_not_found":
                return "Usuário não encontrado.";
            case "account_expired":
                return "Sua conta expirou. Entre em contato com o administrador.";
            case "account_locked":
                return "Sua conta está bloqueada. Tente novamente mais tarde.";
            case "account_disabled":
                return "Sua conta está desabilitada. Entre em contato com o administrador.";
            case "credentials_expired":
                return "Suas credenciais expiraram. Altere sua senha.";
            case "unauthorized":
                return "Você precisa fazer login para acessar esta página.";
            case "authentication_error":
                return "Erro de autenticação. Tente novamente.";
            default:
                return "Erro de login. Tente novamente.";
        }
    }
}
