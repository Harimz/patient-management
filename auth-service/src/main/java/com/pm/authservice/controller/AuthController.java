package com.pm.authservice.controller;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.dto.LoginResponseDTO;
import com.pm.authservice.dto.RegisterUserRequestDTO;
import com.pm.authservice.dto.RegisterUserResponseDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.security.AuthUserDetails;
import com.pm.authservice.service.AuthService;
import com.pm.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Value("${app.cookies.secure:false}")
    private boolean cookieSecure;

    @Value("${app.cookies.sameSite:Lax}")
    private String cookieSameSite;

    @Value("${app.cookies.refreshName:refresh_token}")
    private String refreshCookieName;

    private final Duration refreshCookieMaxAge = Duration.ofDays(4);

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        UserDetails userDetails = authService.authenticate(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        String accessToken = authService.generateAccessToken(userDetails);

        String refreshToken = authService.issueOrReplaceRefreshToken((AuthUserDetails) userDetails);

        setRefreshCookie(response, refreshToken);

        LoginResponseDTO loginResponse = LoginResponseDTO.builder()
                .token(accessToken)
                .expiresIn(900)
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(
            @CookieValue(name = "${app.cookies.refreshName:refresh_token}", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthService.RefreshResult refreshed = authService.refresh(refreshToken);

        setRefreshCookie(response, refreshed.refreshToken());

        LoginResponseDTO loginResponse = LoginResponseDTO.builder()
                .token(refreshed.accessToken())
                .expiresIn(refreshed.expiresIn())
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    @Operation(summary = "Create a user entity")
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        User user = userService.createUser(registerUserRequestDTO);

        RegisterUserResponseDTO response = RegisterUserResponseDTO
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();

        return ResponseEntity.ok(response);
    }

    private void setRefreshCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(refreshCookieName, refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/api")
                .maxAge(refreshCookieMaxAge)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
