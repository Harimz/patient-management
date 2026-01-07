package com.pm.patientservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {

    private String id;
    private String mrn;

    private String firstName;
    private String lastName;
    private String fullName;

    private String gender;
    private String status;
    private String preferredLanguage;

    private String email;
    private String phoneNumber;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private String dateOfBirth;
    private String registeredDate;
    private String createdAt;
    private String updatedAt;

    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
}
