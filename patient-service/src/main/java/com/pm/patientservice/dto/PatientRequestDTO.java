package com.pm.patientservice.dto;

import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDTO {

    @NotBlank
    @Size(max = 80)
    private String firstName;

    @NotBlank
    @Size(max = 80)
    private String lastName;

    @NotNull
    private String gender;

    private String preferredLanguage;

    @NotBlank(groups = CreatePatientValidationGroup.class, message = "Email is required")
    @Email
    private String email;

    @NotBlank
    @Size(max = 32)
    private String phoneNumber;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String country;

    @NotBlank
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "dateOfBirth must be yyyy-MM-dd"
    )
    private String dateOfBirth;

    @NotBlank(groups = CreatePatientValidationGroup.class, message = "Registered date is required")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "registeredDate must be yyyy-MM-dd"
    )
    private String registeredDate;

    @NotBlank
    private String emergencyContactName;

    @NotBlank
    private String emergencyContactPhone;

    @NotBlank
    private String emergencyContactRelation;
}
