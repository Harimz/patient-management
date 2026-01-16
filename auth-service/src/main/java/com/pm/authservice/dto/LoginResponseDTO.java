package com.pm.authservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@Setter
@Builder
public class LoginResponseDTO {

    private final String token;
    private long expiresIn;
}
