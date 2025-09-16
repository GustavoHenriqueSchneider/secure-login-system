package com.securelogin.config;

import com.securelogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Configuração de Segurança do Spring Security
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF para APIs REST (opcional, pode ser habilitado)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configuração de autorização
            .authorizeHttpRequests(auth -> auth
                // Recursos públicos
                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/register/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/favicon.ico",
                    "/error"
                ).permitAll()
                
                // Rotas administrativas
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Rotas de usuário autenticado
                .requestMatchers("/dashboard/**", "/profile/**").hasAnyRole("USER", "ADMIN")
                
                // APIs públicas (se necessário)
                .requestMatchers("/api/public/**").permitAll()
                
                // APIs protegidas
                .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")
                
                // Todas as outras requisições requerem autenticação
                .anyRequest().authenticated()
            )
            
            // Configuração de login simplificada
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            
            // Configuração de logout simplificada
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            
            // Configuração de sessão
            .sessionManagement(session -> session
                .maximumSessions(1) // Apenas uma sessão por usuário
                .maxSessionsPreventsLogin(false) // Permite nova sessão, invalida a anterior
                .expiredUrl("/login?expired")
            )
            
            // Configuração de headers de segurança
            .headers(headers -> headers
                .frameOptions().deny()
                .contentTypeOptions().and()
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .maxAgeInSeconds(31536000)
                )
            )
            
            // Configuração de exceções
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/access-denied")
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/login?error=unauthorized");
                })
            );
        
        return http.build();
    }
}
