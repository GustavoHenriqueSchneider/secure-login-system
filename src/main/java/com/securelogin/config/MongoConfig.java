package com.securelogin.config;

import com.securelogin.entity.Role;
import com.securelogin.entity.User;
import com.securelogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MongoConfig {
    
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Inicializa dados básicos do sistema
     */
    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            log.info("Inicializando dados do sistema...");
            
            // Cria coleções se não existirem
            createCollectionsIfNotExists();
            
            // Cria usuário administrador padrão
            createDefaultAdminUser();
            
            // Cria roles padrão
            createDefaultRoles();
            
            log.info("Inicialização de dados concluída com sucesso!");
        };
    }
    
    /**
     * Cria coleções necessárias se não existirem
     */
    private void createCollectionsIfNotExists() {
        String[] collections = {"users", "roles", "login_attempts"};
        
        for (String collection : collections) {
            if (!mongoTemplate.collectionExists(collection)) {
                mongoTemplate.createCollection(collection);
                log.info("Coleção '{}' criada com sucesso", collection);
            }
        }
    }
    
    /**
     * Cria usuário administrador padrão se não existir
     */
    private void createDefaultAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            log.info("Criando usuário administrador padrão...");
            
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@securelogin.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrador do Sistema");
            admin.setRoles(new HashSet<>(Arrays.asList("ADMIN", "USER")));
            admin.setActive(true);
            admin.setAccountNonExpired(true);
            admin.setAccountNonLocked(true);
            admin.setCredentialsNonExpired(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            
            userRepository.save(admin);
            log.info("Usuário administrador criado com sucesso!");
        } else {
            log.info("Usuário administrador já existe");
        }
    }
    
    /**
     * Cria roles padrão do sistema
     */
    private void createDefaultRoles() {
        String[] roleNames = {"ADMIN", "USER", "MODERATOR"};
        String[] roleDescriptions = {
            "Administrador do sistema com acesso total",
            "Usuário comum do sistema",
            "Moderador com permissões limitadas"
        };
        
        for (int i = 0; i < roleNames.length; i++) {
            String roleName = roleNames[i];
            
            // Verifica se a role já existe
            boolean roleExists = mongoTemplate.collectionExists("roles") &&
                    mongoTemplate.findById(roleName, Role.class, "roles") != null;
            
            if (!roleExists) {
                Role role = new Role();
                role.setName(roleName);
                role.setDescription(roleDescriptions[i]);
                
                try {
                    mongoTemplate.save(role, "roles");
                    log.info("Role '{}' criada com sucesso", roleName);
                } catch (Exception e) {
                    log.warn("Role '{}' já existe ou erro ao criar: {}", roleName, e.getMessage());
                }
            }
        }
    }
}
