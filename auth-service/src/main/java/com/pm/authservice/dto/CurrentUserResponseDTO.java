package com.pm.authservice.dto;

import com.pm.authservice.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class CurrentUserResponseDTO {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
}
