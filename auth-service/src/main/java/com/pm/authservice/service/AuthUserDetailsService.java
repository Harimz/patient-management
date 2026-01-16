package com.pm.authservice.service;

import com.pm.authservice.model.User;
import com.pm.authservice.repository.UserRepository;
import com.pm.authservice.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        return new AuthUserDetails(user);
    }
}
