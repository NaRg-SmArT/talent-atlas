package com.mhosler.TalentAtlas.user;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    public User toUser(UserRequestDto userRequestDto) {
        return new User(
                userRequestDto.getFullName(),
                userRequestDto.getEmail(),
                userRequestDto.getPassword(),
                LocalDateTime.now()
        );
    }
}

