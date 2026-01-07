package com.pm.patientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "patients",
        indexes = {
                @Index(name = "idx_patients_email", columnList = "email"),
                @Index(name = "idx_patients_mrn", columnList = "mrn"),
                @Index(name = "idx_patients_phone", columnList = "phoneNumber")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    @NotBlank
    private String mrn;

    @Column(nullable = false, length = 80)
    @NotBlank
    private String firstName;

    @Column(nullable = false, length = 80)
    @NotBlank
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull
    private PatientStatus status;

    @Column(length = 10)
    private String preferredLanguage;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 32)
    @NotBlank
    private String phoneNumber;

    @Column(nullable = false, length = 120)
    @NotBlank
    private String addressLine1;

    @Column(length = 120)
    private String addressLine2;

    @Column(nullable = false, length = 80)
    @NotBlank
    private String city;

    @Column(nullable = false, length = 80)
    @NotBlank
    private String state;

    @Column(nullable = false, length = 20)
    @NotBlank
    private String postalCode;

    @Column(nullable = false, length = 80)
    @NotBlank
    private String country;

    @NotNull
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 120)
    @NotBlank
    private String emergencyContactName;

    @Column(nullable = false, length = 32)
    @NotBlank
    private String emergencyContactPhone;

    @Column(nullable = false, length = 40)
    @NotBlank
    private String emergencyContactRelation;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @NotNull
    private LocalDate registeredDate;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) this.status = PatientStatus.ACTIVE;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }

    public enum PatientStatus {
        ACTIVE, INACTIVE, ARCHIVED, DECEASED
    }
}
