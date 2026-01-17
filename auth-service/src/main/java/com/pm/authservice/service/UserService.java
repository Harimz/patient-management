package com.pm.authservice.service;

import com.pm.authservice.dto.RegisterUserRequestDTO;
import com.pm.authservice.exceptions.UserAlreadyExistsException;
import com.pm.authservice.exceptions.UserNotFoundException;
import com.pm.authservice.model.User;
import com.pm.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    @Transactional
    public User createUser(RegisterUserRequestDTO userDTO) {
        if (userRepository.existsByEmailIgnoreCase(userDTO.getEmail())) {
            throw new UserAlreadyExistsException(userDTO.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User newUser = User.builder()
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .password(encodedPassword)
                .build();

        return userRepository.save(newUser);
    }
}
