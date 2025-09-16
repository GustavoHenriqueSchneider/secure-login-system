package com.securelogin.repository;

import com.securelogin.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/test-db"
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setFullName("Test User");
        testUser.setActive(true);
        testUser.setRoles(new HashSet<>(Arrays.asList("USER")));
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        userRepository.save(testUser);

        Optional<User> result = userRepository.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenUserNotExists() {
        Optional<User> result = userRepository.findByUsername("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        userRepository.save(testUser);

        Optional<User> result = userRepository.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailNotExists() {
        Optional<User> result = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(result.isPresent());
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUsernameExists() {
        userRepository.save(testUser);

        boolean result = userRepository.existsByUsername("testuser");

        assertTrue(result);
    }

    @Test
    void existsByUsername_ShouldReturnFalse_WhenUsernameNotExists() {
        boolean result = userRepository.existsByUsername("nonexistent");

        assertFalse(result);
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        userRepository.save(testUser);

        boolean result = userRepository.existsByEmail("test@example.com");

        assertTrue(result);
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailNotExists() {
        boolean result = userRepository.existsByEmail("nonexistent@example.com");

        assertFalse(result);
    }

    @Test
    void findByIsActiveTrue_ShouldReturnActiveUsers() {
        User inactiveUser = new User();
        inactiveUser.setUsername("inactiveuser");
        inactiveUser.setEmail("inactive@example.com");
        inactiveUser.setPassword("password123");
        inactiveUser.setFullName("Inactive User");
        inactiveUser.setActive(false);
        inactiveUser.setRoles(new HashSet<>(Arrays.asList("USER")));

        userRepository.save(testUser);
        userRepository.save(inactiveUser);

        List<User> result = userRepository.findByIsActiveTrue();

        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        assertTrue(result.get(0).isActive());
    }

    @Test
    void findByRole_ShouldReturnUsersWithRole() {
        User adminUser = new User();
        adminUser.setUsername("adminuser");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("password123");
        adminUser.setFullName("Admin User");
        adminUser.setActive(true);
        adminUser.setRoles(new HashSet<>(Arrays.asList("ADMIN", "USER")));

        userRepository.save(testUser);
        userRepository.save(adminUser);

        List<User> result = userRepository.findByRole("ADMIN");

        assertEquals(1, result.size());
        assertEquals("adminuser", result.get(0).getUsername());
        assertTrue(result.get(0).getRoles().contains("ADMIN"));
    }

    @Test
    void findByCreatedAtBetween_ShouldReturnUsersCreatedInPeriod() {
        User oldUser = new User();
        oldUser.setUsername("olduser");
        oldUser.setEmail("old@example.com");
        oldUser.setPassword("password123");
        oldUser.setFullName("Old User");
        oldUser.setActive(true);
        oldUser.setRoles(new HashSet<>(Arrays.asList("USER")));
        oldUser.setCreatedAt(LocalDateTime.now().minusDays(10));

        userRepository.save(testUser);
        userRepository.save(oldUser);

        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        List<User> result = userRepository.findByCreatedAtBetween(startDate, endDate);

        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
    }
}
