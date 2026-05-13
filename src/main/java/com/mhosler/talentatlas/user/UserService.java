package com.mhosler.TalentAtlas.user;

import com.mhosler.TalentAtlas.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public UserResponseDto getUserById(Long id) {
        return userMapper.toUserResponseDto(findEntityById(id));
    }

    public UserResponseDto getCurrentUser(Principal principal) {
        return userMapper.toUserResponseDto(findEntityByEmail(principal.getName()));
    }

    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalStateException("An account with that email already exists.");
        }

        User user = userMapper.toUser(userRequestDto);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User existingUser = findEntityById(id);

        String newEmail = userRequestDto.getEmail();
        if (!existingUser.getEmail().equalsIgnoreCase(newEmail)
                && userRepository.existsByEmail(newEmail)) {
            throw new IllegalStateException("That email is already in use.");
        }

        existingUser.setEmail(newEmail);
        existingUser.setFullName(userRequestDto.getFullName());

        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isBlank()) {
            existingUser.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toUserResponseDto(updatedUser);
    }

    public void deleteUser(Long id) {
        User existingUser = findEntityById(id);

        if (!existingUser.getApplications().isEmpty() || !existingUser.getSkills().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete user with existing applications or skills. Delete applications and skills first."
            );
        }

        userRepository.delete(existingUser);
    }
}
