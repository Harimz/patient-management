package com.pm.authservice.service;

import com.pm.authservice.model.RefreshToken;
import com.pm.authservice.model.User;
import com.pm.authservice.repository.RefreshTokenRepository;
import com.pm.authservice.security.AuthUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUserDetailsService authUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${refresh.pepper}")
    private String refreshPepper;

    @Value("${auth.access.expiry-ms:900000}")
    private Long accessExpiryMs;

    @Value("${auth.refresh.days:14}")
    private int refreshDays;

    public record RefreshResult(String accessToken, long expiresIn, String refreshToken) {}

    private final SecureRandom secureRandom = new SecureRandom();

    public UserDetails authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        return authUserDetailsService.loadUserByUsername(email);
    }

    public String generateAccessToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiryMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String issueOrReplaceRefreshToken(AuthUserDetails user) {
        String raw = newRefreshToken();
        String hash = sha256Hex(raw + refreshPepper);

        Instant expiresAt = Instant.now().plus(Duration.ofDays(refreshDays));

        refreshTokenRepository.findByUserId(user.getId()).ifPresent(refreshToken -> {
            refreshToken.setTokenHash(hash);
            refreshToken.setExpiresAt(expiresAt);
            refreshTokenRepository.save(refreshToken);
        });

        if (refreshTokenRepository.findByUserId(user.getId()).isEmpty()) {
            RefreshToken rt = RefreshToken.builder()
                    .userId(user.getId())
                    .tokenHash(hash)
                    .expiresAt(expiresAt)
                    .createdAt(Instant.now())
                    .build();

            refreshTokenRepository.save(rt);
        }

        return raw;
    }

    public RefreshResult refresh(String refreshToken) {
        String hash = sha256Hex(refreshToken + refreshPepper);

        RefreshToken existing = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (Instant.now().isAfter(existing.getExpiresAt())) {
            refreshTokenRepository.deleteById(existing.getId());
            throw new RuntimeException("Refresh token expired");
        }

        User user = userService.findById(existing.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = authUserDetailsService.loadUserByUsername(user.getEmail());

        String newRaw = newRefreshToken();
        existing.setTokenHash(sha256Hex(newRaw + refreshPepper));
        existing.setExpiresAt(Instant.now().plus(Duration.ofDays(refreshDays)));
        refreshTokenRepository.save(existing);

        String newAccess = generateAccessToken(userDetails);

        return new RefreshResult(newAccess, accessExpiryMs / 1000, newRaw);
    }

    // Helpers

    private Key getSigningKey() {
        byte[] keyBytes =  secretKey.getBytes();

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String newRefreshToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
