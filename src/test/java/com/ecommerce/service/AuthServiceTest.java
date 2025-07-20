package com.ecommerce.service;

import com.ecommerce.dto.auth.AuthRequestDTO;
import com.ecommerce.dto.auth.AuthResponseDTO;
import com.ecommerce.dto.auth.RegisterRequestDTO;
import com.ecommerce.entity.User;
import com.ecommerce.enums.UserRole;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User user;
    private AuthRequestDTO authRequestDTO;
    private RegisterRequestDTO registerRequestDTO;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        
        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("encodedPassword");
        user.setRole(UserRole.USER);

        authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setEmail("test@example.com");
        authRequestDTO.setPassword("password");

        registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail("new@example.com");
        registerRequestDTO.setPassword("password");
        registerRequestDTO.setName("New User");
    }

    @Test
    void login_ShouldReturnAuthResponseDTO() {
        // Arrange
        String token = "jwt.token.here";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(tokenProvider.generateToken(authentication)).thenReturn(token);

        // Act
        AuthResponseDTO result = authService.login(authRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.getToken());
        assertEquals(userId, result.getUserId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getRole(), result.getRole());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("test@example.com");
        verify(tokenProvider).generateToken(authentication);
    }

    @Test
    void login_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(tokenProvider.generateToken(authentication)).thenReturn("token");

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.login(authRequestDTO));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("test@example.com");
        verify(tokenProvider).generateToken(authentication);
    }

    @Test
    void register_ShouldCreateUserAndReturnAuthResponseDTO() {
        // Arrange
        String token = "jwt.token.here";
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn(token);

        // Act
        AuthResponseDTO result = authService.register(registerRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.getToken());
        assertEquals(userId, result.getUserId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getRole(), result.getRole());
        verify(userRepository).existsByEmail("new@example.com");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken(authentication);
    }

    @Test
    void register_WhenEmailAlreadyExists_ShouldThrowException() {
        // Arrange
        when(userRepository.existsByEmail("new@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.register(registerRequestDTO));
        verify(userRepository).existsByEmail("new@example.com");
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        UserDetails result = authService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertTrue(result.isEnabled());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isCredentialsNonExpired());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername("test@example.com"));
        verify(userRepository).findByEmail("test@example.com");
    }
} 