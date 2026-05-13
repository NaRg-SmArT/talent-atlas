package com.mhosler.TalentAtlas.user;

import com.mhosler.TalentAtlas.application.ApplicationRecord;
import com.mhosler.TalentAtlas.exception.ResourceNotFoundException;
import com.mhosler.TalentAtlas.skill.Skill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void findEntityById_shouldReturnUser_whenUserExists() {
        User user = new User("Mary Hosler", "mary@example.com", "hash", LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findEntityById(1L);

        assertNotNull(result);
        assertEquals("mary@example.com", result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void findEntityById_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findEntityById(99L)
        );

        assertEquals("User not found: 99", ex.getMessage());
        verify(userRepository).findById(99L);
    }

    @Test
    void findEntityByEmail_shouldReturnUser_whenUserExists() {
        User user = new User("Mary Hosler", "mary@example.com", "hash", LocalDateTime.now());

        when(userRepository.findByEmail("mary@example.com")).thenReturn(Optional.of(user));

        User result = userService.findEntityByEmail("mary@example.com");

        assertNotNull(result);
        assertEquals("Mary Hosler", result.getFullName());
        verify(userRepository).findByEmail("mary@example.com");
    }

    @Test
    void findEntityByEmail_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findEntityByEmail("missing@example.com")
        );

        assertEquals("User not found with email: missing@example.com", ex.getMessage());
        verify(userRepository).findByEmail("missing@example.com");
    }

    @Test
    void getUserById_shouldReturnMappedResponseDto() {
        User user = new User("Mary Hosler", "mary@example.com", "hash", LocalDateTime.now());
        UserResponseDto responseDto =
                new UserResponseDto(1L, "Mary Hosler", "mary@example.com", user.getCreatedAt());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("Mary Hosler", result.getFullName());
        assertEquals("mary@example.com", result.getEmail());
        verify(userRepository).findById(1L);
        verify(userMapper).toUserResponseDto(user);
    }

    @Test
    void getCurrentUser_shouldReturnMappedResponseDto() {
        Principal principal = () -> "mary@example.com";
        User user = new User("Mary Hosler", "mary@example.com", "hash", LocalDateTime.now());
        UserResponseDto responseDto =
                new UserResponseDto(1L, "Mary Hosler", "mary@example.com", user.getCreatedAt());

        when(userRepository.findByEmail("mary@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.getCurrentUser(principal);

        assertNotNull(result);
        assertEquals("mary@example.com", result.getEmail());
        verify(userRepository).findByEmail("mary@example.com");
        verify(userMapper).toUserResponseDto(user);
    }

    @Test
    void addUser_shouldEncodePasswordSaveUserAndReturnResponseDto() {
        UserRequestDto requestDto =
                new UserRequestDto("Mary Hosler", "mary@example.com", "password123");

        User mappedUser =
                new User("Mary Hosler", "mary@example.com", "password123", LocalDateTime.now());

        User savedUser =
                new User("Mary Hosler", "mary@example.com", "encodedPassword", mappedUser.getCreatedAt());

        UserResponseDto responseDto =
                new UserResponseDto(1L, "Mary Hosler", "mary@example.com", savedUser.getCreatedAt());

        when(userRepository.existsByEmail("mary@example.com")).thenReturn(false);
        when(userMapper.toUser(requestDto)).thenReturn(mappedUser);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(mappedUser)).thenReturn(savedUser);
        when(userMapper.toUserResponseDto(savedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.addUser(requestDto);

        assertNotNull(result);
        assertEquals("mary@example.com", result.getEmail());
        assertEquals("encodedPassword", mappedUser.getPasswordHash());
        verify(userRepository).existsByEmail("mary@example.com");
        verify(userMapper).toUser(requestDto);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(mappedUser);
        verify(userMapper).toUserResponseDto(savedUser);
    }

    @Test
    void addUser_shouldThrowIllegalStateException_whenEmailAlreadyExists() {
        UserRequestDto requestDto =
                new UserRequestDto("Mary Hosler", "mary@example.com", "password123");

        when(userRepository.existsByEmail("mary@example.com")).thenReturn(true);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> userService.addUser(requestDto)
        );

        assertEquals("An account with that email already exists.", ex.getMessage());
        verify(userRepository).existsByEmail("mary@example.com");
        verify(userMapper, never()).toUser(any());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_shouldUpdateNameAndEmailWithoutChangingPassword_whenPasswordIsBlank() {
        UserRequestDto requestDto =
                new UserRequestDto("Updated Name", "updated@example.com", "");

        User existingUser =
                new User("Old Name", "old@example.com", "oldHash", LocalDateTime.now());

        UserResponseDto responseDto =
                new UserResponseDto(1L, "Updated Name", "updated@example.com", existingUser.getCreatedAt());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toUserResponseDto(existingUser)).thenReturn(responseDto);

        UserResponseDto result = userService.updateUser(1L, requestDto);

        assertNotNull(result);
        assertEquals("Updated Name", existingUser.getFullName());
        assertEquals("updated@example.com", existingUser.getEmail());
        assertEquals("oldHash", existingUser.getPasswordHash());
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("updated@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository).save(existingUser);
        verify(userMapper).toUserResponseDto(existingUser);
    }

    @Test
    void updateUser_shouldEncodeNewPassword_whenPasswordIsProvided() {
        UserRequestDto requestDto =
                new UserRequestDto("Updated Name", "updated@example.com", "newPassword123");

        User existingUser =
                new User("Old Name", "old@example.com", "oldHash", LocalDateTime.now());

        UserResponseDto responseDto =
                new UserResponseDto(1L, "Updated Name", "updated@example.com", existingUser.getCreatedAt());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedHash");
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toUserResponseDto(existingUser)).thenReturn(responseDto);

        UserResponseDto result = userService.updateUser(1L, requestDto);

        assertNotNull(result);
        assertEquals("Updated Name", existingUser.getFullName());
        assertEquals("updated@example.com", existingUser.getEmail());
        assertEquals("newEncodedHash", existingUser.getPasswordHash());
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("updated@example.com");
        verify(passwordEncoder).encode("newPassword123");
        verify(userRepository).save(existingUser);
        verify(userMapper).toUserResponseDto(existingUser);
    }

    @Test
    void updateUser_shouldNotCheckDuplicateEmail_whenEmailDidNotActuallyChange() {
        UserRequestDto requestDto =
                new UserRequestDto("Updated Name", "old@example.com", "");

        User existingUser =
                new User("Old Name", "old@example.com", "oldHash", LocalDateTime.now());

        UserResponseDto responseDto =
                new UserResponseDto(1L, "Updated Name", "old@example.com", existingUser.getCreatedAt());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toUserResponseDto(existingUser)).thenReturn(responseDto);

        UserResponseDto result = userService.updateUser(1L, requestDto);

        assertNotNull(result);
        assertEquals("Updated Name", existingUser.getFullName());
        assertEquals("old@example.com", existingUser.getEmail());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository).save(existingUser);
        verify(userMapper).toUserResponseDto(existingUser);
    }

    @Test
    void updateUser_shouldThrowIllegalStateException_whenNewEmailAlreadyInUse() {
        UserRequestDto requestDto =
                new UserRequestDto("Updated Name", "taken@example.com", "password123");

        User existingUser =
                new User("Old Name", "old@example.com", "oldHash", LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> userService.updateUser(1L, requestDto)
        );

        assertEquals("That email is already in use.", ex.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("taken@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_shouldDeleteUser_whenUserHasNoApplicationsOrSkills() {
        User existingUser =
                new User("Mary Hosler", "mary@example.com", "hash", LocalDateTime.now());
        existingUser.setApplications(new ArrayList<>());
        existingUser.setSkills(new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(existingUser);
    }

    @Test
    void deleteUser_shouldThrowIllegalStateException_whenUserHasApplications() {
        User existingUser =
                new User("Mary Hosler", "mary@example.com", "hash", LocalDateTime.now());
        existingUser.getApplications().add(mock(ApplicationRecord.class));

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> userService.deleteUser(1L)
        );

        assertEquals(
                "Cannot delete user with existing applications or skills. Delete applications and skills first.",
                ex.getMessage()
        );
        verify(userRepository).findById(1L);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUser_shouldThrowIllegalStateException_whenUserHasSkills() {
        User existingUser =
                new User("Mary Hosler", "mary@example.com", "hash", LocalDateTime.now());
        existingUser.getSkills().add(mock(Skill.class));

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> userService.deleteUser(1L)
        );

        assertEquals(
                "Cannot delete user with existing applications or skills. Delete applications and skills first.",
                ex.getMessage()
        );
        verify(userRepository).findById(1L);
        verify(userRepository, never()).delete(any());
    }
}

