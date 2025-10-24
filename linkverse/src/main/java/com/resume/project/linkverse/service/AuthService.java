package com.resume.project.linkverse.service;

import com.resume.project.linkverse.dto.AuthDtos;
import com.resume.project.linkverse.entity.Role;
import com.resume.project.linkverse.entity.User;
import com.resume.project.linkverse.exception.ResourceNotFoundException;
import com.resume.project.linkverse.repository.RoleRepository;
import com.resume.project.linkverse.repository.UserRepository;
import com.resume.project.linkverse.security.JwtService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthDtos.UserDto registerUser(AuthDtos.RegisterRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new ValidationException("Email is already in use.");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found. Please seed the database."));

        User user = new User();
        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());
        user.setEmail(requestDto.email());
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);

        return AuthDtos.UserDto.fromUser(savedUser);
    }


    public AuthDtos.AuthResponseDto loginUser(AuthDtos.LoginRequestDto requestDto) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.email(),
                        requestDto.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        User user = (User) authentication.getPrincipal();


        String jwt = jwtService.generateToken(user);

        return new AuthDtos.AuthResponseDto(jwt, AuthDtos.UserDto.fromUser(user));
    }


    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new SecurityException("No authenticated user found.");
        }
        String email = ((User) authentication.getPrincipal()).getEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in database."));
    }
}