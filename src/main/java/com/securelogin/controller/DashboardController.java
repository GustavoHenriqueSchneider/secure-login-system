package com.securelogin.controller;

import com.securelogin.entity.User;
import com.securelogin.service.LoginAttemptService;
import com.securelogin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    
    private final UserService userService;
    private final LoginAttemptService loginAttemptService;
    
    @GetMapping
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        log.debug("Acessando dashboard para usuário: {}", username);
        
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        List<com.securelogin.entity.LoginAttempt> recentLogins = 
                loginAttemptService.getSuccessfulLoginAttemptsByUsername(username);
        
        LoginAttemptService.SecurityReport securityReport = loginAttemptService.generateSecurityReport();
        
        model.addAttribute("user", user);
        model.addAttribute("recentLogins", recentLogins.subList(0, Math.min(5, recentLogins.size())));
        model.addAttribute("securityReport", securityReport);
        
        return "dashboard/index";
    }
    
    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        String username = authentication.getName();
        log.debug("Acessando perfil para usuário: {}", username);
        
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        model.addAttribute("user", user);
        
        return "dashboard/profile";
    }
}
