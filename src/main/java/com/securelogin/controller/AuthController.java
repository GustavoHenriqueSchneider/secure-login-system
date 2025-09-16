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

import java.util.Set;

/**
 * Controlador para operações de autenticação
 */
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
    
    /**
     * Exibe a página de cadastro
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        // Verifica se o usuário já está autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "auth/register";
    }
    
    /**
     * Processa o cadastro de novo usuário
     */
    @PostMapping("/register")
    public String registerUser(@Valid UserRegistrationDto userRegistrationDto, 
                              BindingResult bindingResult, 
                              RedirectAttributes redirectAttributes) {
        
        log.info("Tentativa de cadastro para usuário: {}", userRegistrationDto.getUsername());
        
        // Validação de dados
        if (bindingResult.hasErrors()) {
            log.warn("Erro de validação no cadastro: {}", bindingResult.getAllErrors());
            return "auth/register";
        }
        
        // Verifica se as senhas coincidem
        if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.userRegistrationDto", "As senhas não coincidem");
            return "auth/register";
        }
        
        try {
            // Cria o usuário
            User user = new User();
            user.setUsername(userRegistrationDto.getUsername());
            user.setEmail(userRegistrationDto.getEmail());
            user.setPassword(userRegistrationDto.getPassword());
            user.setFullName(userRegistrationDto.getFullName());
            user.setRoles(Set.of("USER")); // Role padrão para novos usuários
            
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
    
    /**
     * Exibe página de acesso negado
     */
    @GetMapping("/access-denied")
    public String accessDeniedPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            model.addAttribute("username", auth.getName());
        }
        return "error/access-denied";
    }
    
    /**
     * Converte códigos de erro em mensagens amigáveis
     */
    private String getErrorMessage(String error) {
        return switch (error) {
            case "invalid_credentials" -> "Nome de usuário ou senha inválidos.";
            case "user_not_found" -> "Usuário não encontrado.";
            case "account_expired" -> "Sua conta expirou. Entre em contato com o administrador.";
            case "account_locked" -> "Sua conta está bloqueada. Tente novamente mais tarde.";
            case "account_disabled" -> "Sua conta está desabilitada. Entre em contato com o administrador.";
            case "credentials_expired" -> "Suas credenciais expiraram. Altere sua senha.";
            case "unauthorized" -> "Você precisa fazer login para acessar esta página.";
            case "authentication_error" -> "Erro de autenticação. Tente novamente.";
            default -> "Erro de login. Tente novamente.";
        };
    }
}
