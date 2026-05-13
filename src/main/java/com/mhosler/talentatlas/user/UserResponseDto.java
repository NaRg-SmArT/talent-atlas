package com.mhosler.TalentAtlas.user;

import java.time.LocalDateTime;


public class UserResponseDto {

                                                                             // Fields
    private Long id;
    private String fullName;
    private String email;
    private LocalDateTime createdAt;


                                                                            // Constructors

    public UserResponseDto(Long id, String fullName, String email, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = createdAt;
    }

                                                                            // Accessors


    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}



