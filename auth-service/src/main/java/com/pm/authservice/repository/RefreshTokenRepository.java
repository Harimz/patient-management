package com.pm.authservice.repository;

import com.pm.authservice.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);
    Optional<RefreshToken> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
