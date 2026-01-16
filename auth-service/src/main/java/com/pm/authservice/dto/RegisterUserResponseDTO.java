package com.pm.authservice.dto;

import com.pm.authservice.model.UserRole;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserResponseDTO {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
}
