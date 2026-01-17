package com.pm.authservice.controller;

import com.pm.authservice.dto.*;
import com.pm.authservice.model.User;
import com.pm.authservice.model.UserRole;
import com.pm.authservice.security.AuthUserDetails;
import com.pm.authservice.service.AuthService;
import com.pm.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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
    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        AuthUserDetails userDetails = authService.authenticate(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        String accessToken = authService.generateAccessToken(userDetails);

        String refreshToken = authService.issueOrReplaceRefreshToken(userDetails);

        setRefreshCookie(response, refreshToken);

        LoginResponseDTO loginResponse = LoginResponseDTO.builder()
                .token(accessToken)
                .expiresIn(900)
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(path = "/refresh")
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
    @PostMapping(path = "/register")
    public ResponseEntity<RegisterUserResponseDTO> registerUser(@Valid @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
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

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponseDTO> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String userIdClaim = jwt.getClaimAsString("userId");
        if (userIdClaim == null || userIdClaim.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UUID userId = UUID.fromString(userIdClaim);

        User user = userService.findById(userId);

        UserRole role = user.getRole();

        CurrentUserResponseDTO currentUser = CurrentUserResponseDTO.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .role(role)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        return ResponseEntity.ok(currentUser);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "${app.cookies.refreshName:refresh_token}", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            authService.logout(refreshToken);
        }

        clearRefreshCookie(response);

        return ResponseEntity.noContent().build();
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

    private void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(refreshCookieName, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/api")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
