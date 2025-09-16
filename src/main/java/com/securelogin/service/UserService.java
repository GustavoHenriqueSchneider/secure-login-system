package com.securelogin.service;

import com.securelogin.entity.User;
import com.securelogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Serviço para gerenciamento de usuários
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOCK_TIME_MINUTES = 30;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        
        if (!user.isActive()) {
            throw new UsernameNotFoundException("Conta inativa: " + username);
        }
        
        return user;
    }
    
    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Nome de usuário já existe: " + user.getUsername());
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email já existe: " + user.getEmail());
        }
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("USER"));
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    /**
     * Atualiza um usuário existente
     */
    @Transactional
    public User updateUser(String id, User userDetails) {
        log.info("Atualizando usuário: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        
        // Atualiza campos permitidos
        if (userDetails.getFullName() != null) {
            user.setFullName(userDetails.getFullName());
        }
        
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new IllegalArgumentException("Email já existe: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());
        }
        
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        if (userDetails.getRoles() != null) {
            user.setRoles(userDetails.getRoles());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        log.info("Usuário atualizado com sucesso: {}", updatedUser.getUsername());
        
        return updatedUser;
    }
    
    /**
     * Busca usuário por ID
     */
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    
    /**
     * Busca usuário por nome de usuário
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Lista todos os usuários
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Lista usuários ativos
     */
    public List<User> findActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
    
    /**
     * Desativa um usuário
     */
    @Transactional
    public void deactivateUser(String id) {
        log.info("Desativando usuário: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        log.info("Usuário desativado com sucesso: {}", user.getUsername());
    }
    
    /**
     * Ativa um usuário
     */
    @Transactional
    public void activateUser(String id) {
        log.info("Ativando usuário: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        log.info("Usuário ativado com sucesso: {}", user.getUsername());
    }
    
    // Métodos de auditoria removidos para simplificar o projeto
    
    /**
     * Desbloqueia conta de usuário
     */
    @Transactional
    public void unlockUser(String id) {
        log.info("Desbloqueando usuário: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        
        user.setAccountNonLocked(true);
        userRepository.save(user);
        
        log.info("Usuário desbloqueado com sucesso: {}", user.getUsername());
    }
    
    /**
     * Verifica se usuário tem role específica
     */
    public boolean hasRole(String username, String role) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().contains(role))
                .orElse(false);
    }
    
    /**
     * Cria usuário administrador padrão se não existir
     */
    @Transactional
    public void createDefaultAdminIfNotExists() {
        if (!userRepository.existsByUsername("admin")) {
            log.info("Criando usuário administrador padrão");
            
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@securelogin.com");
            admin.setPassword("admin123"); // Será criptografada
            admin.setFullName("Administrador do Sistema");
            admin.setRoles(Set.of("ADMIN", "USER"));
            
            createUser(admin);
            log.info("Usuário administrador criado com sucesso");
        }
    }
}
