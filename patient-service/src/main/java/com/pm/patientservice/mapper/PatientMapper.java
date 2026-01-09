package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = { java.time.LocalDate.class })
public interface PatientMapper {

    @Mapping(target = "id", expression = "java(toString(patient.getId()))")
    @Mapping(target = "fullName", expression = "java(patient.getFirstName() + \" \" + patient.getLastName())")
    @Mapping(target = "gender", expression = "java(patient.getGender().name())")
    @Mapping(target = "status", expression = "java(patient.getStatus().name())")
    @Mapping(target = "dateOfBirth", expression = "java(toString(patient.getDateOfBirth()))")
    @Mapping(target = "createdAt", expression = "java(toString(patient.getCreatedAt()))")
    @Mapping(target = "updatedAt", expression = "java(toString(patient.getUpdatedAt()))")
    PatientResponseDTO toDTO(Patient patient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "mrn", expression = "java(generateMrn())")
    @Mapping(target = "gender", expression = "java(parseGender(dto.getGender()))")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "dateOfBirth", expression = "java(LocalDate.parse(dto.getDateOfBirth()))")
    @Mapping(target = "registeredDate", expression = "java(LocalDate.parse(dto.getRegisteredDate()))")
    Patient toModel(PatientRequestDTO dto);

    default String toString(Object v) {
        return v == null ? null : v.toString();
    }

    default Patient.Gender parseGender(String value) {
        return Patient.Gender.valueOf(value.trim().toUpperCase());
    }

    default String generateMrn() {
        return "MRN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
