package com.securelogin.service;

import com.securelogin.entity.User;
import com.securelogin.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User inactiveUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("1");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setFullName("Test User");
        testUser.setActive(true);
        testUser.setRoles(new HashSet<>(Arrays.asList("USER")));
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());

        inactiveUser = new User();
        inactiveUser.setId("2");
        inactiveUser.setUsername("inactiveuser");
        inactiveUser.setEmail("inactive@example.com");
        inactiveUser.setPassword("password123");
        inactiveUser.setFullName("Inactive User");
        inactiveUser.setActive(false);
        inactiveUser.setRoles(new HashSet<>(Arrays.asList("USER")));
    }

    @Test
    void loadUserByUsername_ShouldReturnUser_WhenUserExistsAndIsActive() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails result = userService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> 
            userService.loadUserByUsername("nonexistent"));
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserIsInactive() {
        when(userRepository.findByUsername("inactiveuser")).thenReturn(Optional.of(inactiveUser));

        assertThrows(UsernameNotFoundException.class, () -> 
            userService.loadUserByUsername("inactiveuser"));
    }

    @Test
    void createUser_ShouldCreateUser_WhenValidData() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("password123");
        newUser.setFullName("New User");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(newUser);

        assertNotNull(result);
        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("new@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameExists() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("password123");
        newUser.setFullName("New User");

        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> 
            userService.createUser(newUser));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailExists() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("test@example.com");
        newUser.setPassword("password123");
        newUser.setFullName("New User");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> 
            userService.createUser(newUser));
    }

    @Test
    void createUser_ShouldSetDefaultRole_WhenNoRolesProvided() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("password123");
        newUser.setFullName("New User");
        newUser.setRoles(null);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals(new HashSet<>(Arrays.asList("USER")), user.getRoles());
            return testUser;
        });

        userService.createUser(newUser);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenValidData() {
        User updateData = new User();
        updateData.setFullName("Updated Name");
        updateData.setEmail("updated@example.com");

        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser("1", updateData);

        assertNotNull(result);
        verify(userRepository).findById("1");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        User updateData = new User();
        updateData.setFullName("Updated Name");

        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> 
            userService.updateUser("999", updateData));
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenUserNotExists() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    void deactivateUser_ShouldDeactivateUser_WhenUserExists() {
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.deactivateUser("1");

        assertFalse(testUser.isActive());
        verify(userRepository).save(testUser);
    }

    @Test
    void activateUser_ShouldActivateUser_WhenUserExists() {
        when(userRepository.findById("2")).thenReturn(Optional.of(inactiveUser));
        when(userRepository.save(any(User.class))).thenReturn(inactiveUser);

        userService.activateUser("2");

        assertTrue(inactiveUser.isActive());
        verify(userRepository).save(inactiveUser);
    }

    @Test
    void hasRole_ShouldReturnTrue_WhenUserHasRole() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        boolean result = userService.hasRole("testuser", "USER");

        assertTrue(result);
    }

    @Test
    void hasRole_ShouldReturnFalse_WhenUserDoesNotHaveRole() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        boolean result = userService.hasRole("testuser", "ADMIN");

        assertFalse(result);
    }

    @Test
    void hasRole_ShouldReturnFalse_WhenUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        boolean result = userService.hasRole("nonexistent", "USER");

        assertFalse(result);
    }

    @Test
    void createDefaultAdminIfNotExists_ShouldCreateAdmin_WhenAdminDoesNotExist() {
        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(userRepository.existsByEmail("admin@securelogin.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.createDefaultAdminIfNotExists();

        verify(userRepository, times(2)).existsByUsername("admin");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createDefaultAdminIfNotExists_ShouldNotCreateAdmin_WhenAdminExists() {
        when(userRepository.existsByUsername("admin")).thenReturn(true);

        userService.createDefaultAdminIfNotExists();

        verify(userRepository).existsByUsername("admin");
        verify(userRepository, never()).save(any(User.class));
    }
}