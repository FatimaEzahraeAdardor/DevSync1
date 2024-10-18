package org.example.devsync1.services;

import org.example.devsync1.entities.Token;
import org.example.devsync1.entities.User;
import org.example.devsync1.enums.Role;
import org.example.devsync1.exeption.UserAlreadyExistException;
import org.example.devsync1.exeption.UserEqualsNullException;
import org.example.devsync1.repositories.implementations.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    static Stream<User>  userGenerate(){
        return Stream.of(
                new User("userTest", "userFirstName", "userLastName", "test@example.com", "password123", Role.USER)
        );
    }
    @Test
    void saveUser_WhenUserIsNull_ShouldThrowUserEqualsNullException() {
        assertThrows(UserEqualsNullException.class, () -> userService.save(null));
    }

    @ParameterizedTest
    @MethodSource("userGenerate")
    void saveUser_WhenUserAlreadyExists_ShouldThrowUserAlreadyExistException(User user) {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyExistException.class, () -> userService.save(user));
    }

    @ParameterizedTest
    @MethodSource("userGenerate")
    void save_WhenUserIsValidAndDoesNotExist_ShouldSaveUser(User user) throws Exception {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        assertTrue(BCrypt.checkpw("password123", savedUser.getPassword()));
        verify(userRepository, times(1)).save(user);
    }

    @ParameterizedTest
    @MethodSource("userGenerate")
    void save_WhenUserRoleIsUSER_ShouldAssignTokenToUser(User user) throws Exception {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        assertEquals(2, savedUser.getTokens().get(0).getModifyTokenCount());
        assertEquals(1, savedUser.getTokens().get(0).getDeleteTokenCount());
        verify(userRepository, times(1)).save(user);
    }
}
