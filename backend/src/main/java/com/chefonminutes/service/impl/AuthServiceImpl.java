package com.chefonminutes.service.impl;

import com.chefonminutes.dto.AuthResponseDTO;
import com.chefonminutes.dto.LoginRequestDTO;
import com.chefonminutes.dto.RegisterRequestDTO;
import com.chefonminutes.dto.UserDTO;
import com.chefonminutes.exception.InvalidStateException;
import com.chefonminutes.exception.ResourceNotFoundException;
import com.chefonminutes.model.ChefProfile;
import com.chefonminutes.model.Role;
import com.chefonminutes.model.User;
import com.chefonminutes.repository.ChefProfileRepository;
import com.chefonminutes.repository.UserRepository;
import com.chefonminutes.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ChefProfileRepository chefProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidStateException("Email already registered: " + request.getEmail());
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        user = userRepository.save(user);

        if (user.getRole() == Role.CHEF) {
            ChefProfile chefProfile = ChefProfile.builder()
                    .user(user)
                    .available(true)
                    .build();
            chefProfileRepository.save(chefProfile);
        }
        return AuthResponseDTO.builder().user(toDTO(user)).build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account for " + request.getEmail()));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidStateException("Invalid email or password");
        }
        return AuthResponseDTO.builder().user(toDTO(user)).build();
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
