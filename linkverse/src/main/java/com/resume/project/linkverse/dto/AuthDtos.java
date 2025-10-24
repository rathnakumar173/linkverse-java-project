package com.resume.project.linkverse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public record RegisterRequestDto(
            @NotEmpty(message = "First name is required")
            String firstName,

            @NotEmpty(message = "Last name is required")
            String lastName,

            @NotEmpty(message = "Email is required")
            @Email(message = "Invalid email format")
            String email,

            @NotEmpty(message = "Password is required")
            @Size(min = 8, message = "Password must be at least 8 characters long")
            String password
    ) {}

    public record LoginRequestDto(
            @NotEmpty(message = "Email is required")
            @Email(message = "Invalid email format")
            String email,

            @NotEmpty(message = "Password is required")
            String password
    ) {}

    public record AuthResponseDto(
            String accessToken,
            UserDto user
    ) {}

    public record UserDto(
            Long id,
            String firstName,
            String lastName,
            String email
    ) {

        public static UserDto fromUser(com.resume.project.linkverse.entity.User user) {
            return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        }
    }
}