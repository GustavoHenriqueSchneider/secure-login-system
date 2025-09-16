package com.securelogin.repository;

import com.securelogin.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    /**
     * Busca usuário por nome de usuário
     * @param username Nome de usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Busca usuário por email
     * @param email Email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Verifica se existe usuário com o nome de usuário especificado
     * @param username Nome de usuário
     * @return true se existe, false caso contrário
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica se existe usuário com o email especificado
     * @param email Email do usuário
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuários ativos
     * @return Lista de usuários ativos
     */
    List<User> findByIsActiveTrue();
    
    /**
     * Busca usuários por role
     * @param role Role a ser buscada
     * @return Lista de usuários com a role especificada
     */
    @Query("{ 'roles': { $in: [?0] } }")
    List<User> findByRole(String role);
    
    /**
     * Busca usuários bloqueados
     * @return Lista de usuários bloqueados
     */
    @Query("{ 'lockedUntil': { $gt: ?0 } }")
    List<User> findLockedUsers(LocalDateTime now);
    
    /**
     * Busca usuários com muitas tentativas de login
     * @param maxAttempts Número máximo de tentativas permitidas
     * @return Lista de usuários com tentativas excedidas
     */
    List<User> findByLoginAttemptsGreaterThan(int maxAttempts);
    
    /**
     * Busca usuários criados em um período específico
     * @param startDate Data de início
     * @param endDate Data de fim
     * @return Lista de usuários criados no período
     */
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
