package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exceptions.EmailAlreadyExistsException;
import com.pm.patientservice.exceptions.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(patientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(patientRequestDTO.getEmail());
        }

        Patient newPatient = patientRepository.save(patientMapper.toModel(patientRequestDTO));


        return patientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient updatedPatient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));

        String newEmail = patientRequestDTO.getEmail();
        if (newEmail != null && !newEmail.isBlank()) {
            if (!newEmail.equalsIgnoreCase(updatedPatient.getEmail())) {
                if (patientRepository.existsByEmailAndIdNot(newEmail, id)) {
                    throw new EmailAlreadyExistsException(newEmail);
                }
                updatedPatient.setEmail(newEmail);
            }
        }

        updatedPatient.setFirstName(patientRequestDTO.getFirstName());
        updatedPatient.setLastName(patientRequestDTO.getLastName());
        updatedPatient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        updatedPatient.setCity(patientRequestDTO.getCity());
        updatedPatient.setState(patientRequestDTO.getState());
        updatedPatient.setCountry(patientRequestDTO.getCountry());
        updatedPatient.setPhoneNumber(patientRequestDTO.getPhoneNumber());
        updatedPatient.setAddressLine1(patientRequestDTO.getAddressLine1());
        updatedPatient.setAddressLine2(patientRequestDTO.getAddressLine2());

        updatedPatient.setEmergencyContactName(patientRequestDTO.getEmergencyContactName());
        updatedPatient.setEmergencyContactPhone(patientRequestDTO.getEmergencyContactPhone());
        updatedPatient.setEmergencyContactRelation(patientRequestDTO.getEmergencyContactRelation());

        Patient savedPatient = patientRepository.save(updatedPatient);

        return patientMapper.toDTO(savedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
