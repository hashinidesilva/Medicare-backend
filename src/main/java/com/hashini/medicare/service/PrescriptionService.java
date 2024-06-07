package com.hashini.medicare.service;

import com.hashini.medicare.dao.MedicineDAO;
import com.hashini.medicare.dao.PatientDAO;
import com.hashini.medicare.dao.PrescriptionDAO;
import com.hashini.medicare.dao.PrescriptionMedicineDAO;
import com.hashini.medicare.dto.PrescriptionCreationDTO;
import com.hashini.medicare.dto.PrescriptionDTO;
import com.hashini.medicare.exception.NotFoundException;
import com.hashini.medicare.model.Prescription;
import com.hashini.medicare.model.PrescriptionMedicine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    private final PrescriptionDAO prescriptionDAO;
    private final PatientDAO patientDAO;
    private final PrescriptionMedicineDAO prescriptionMedicineDAO;
    private final MedicineDAO medicineDAO;

    public PrescriptionService(PrescriptionDAO prescriptionDAO,
                               PatientDAO patientDAO,
                               PrescriptionMedicineDAO prescriptionMedicineDAO,
                               MedicineDAO medicineDAO) {
        this.prescriptionDAO = prescriptionDAO;
        this.patientDAO = patientDAO;
        this.prescriptionMedicineDAO = prescriptionMedicineDAO;
        this.medicineDAO = medicineDAO;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    public long addPrescription(PrescriptionCreationDTO prescriptionInfo) {
        return patientDAO.selectPatientById(prescriptionInfo.getPatientId())
                .map(patient -> {
                    long prescriptionId = prescriptionDAO.addPrescription(new Prescription(prescriptionInfo.getPatientId(),
                            prescriptionInfo.getDiagnosis(), prescriptionInfo.getHistory()));
                    prescriptionInfo.getMedicines()
                            .forEach(medicine -> medicineDAO.selectMedicineById(medicine.getMedicineId())
                                    .map(medicineDTO -> {
                                        PrescriptionMedicine prescriptionMedicine = new PrescriptionMedicine(prescriptionId,
                                                medicineDTO.getId(),
                                                medicine.getDose(),
                                                medicine.getDuration(),
                                                medicine.getFrequency(),
                                                medicine.getFrequencyText(),
                                                medicine.getQuantity(),
                                                medicine.getAdditionalInfo());
                                        prescriptionMedicineDAO.addPrescriptionMedicine(prescriptionMedicine);
                                        medicineDAO.updateUnits(medicineDTO.getId(), medicine.getQuantity());
                                        return prescriptionMedicine;
                                    })
                                    .orElseThrow(() -> new NotFoundException("Medicine id = " + medicine.getMedicineId() + " not found")));
                    return prescriptionId;
                })
                .orElseThrow(() -> new NotFoundException("Patient with id = " + prescriptionInfo.getPatientId() + " is not found"));
    }

    public List<PrescriptionDTO> getAllPrescriptions(Optional<Boolean> processed) {
        return processed.map(prescriptionDAO::selectPrescriptionsByProcessed)
                .orElseGet(prescriptionDAO::selectPrescriptions);
    }

    public PrescriptionDTO getPrescription(long id) {
        return prescriptionDAO.selectPrescriptionById(id)
                .orElseThrow(() -> new NotFoundException("Prescription with id = " + id + " not found"));
    }

    public long updatePrescription(Prescription newPrescription, long prescriptionId) {
        return prescriptionDAO.selectPrescriptionById(prescriptionId)
                .map(prescription -> prescriptionDAO.updatePrescription(newPrescription, prescriptionId))
                .orElseGet(() -> {
                    newPrescription.setId(prescriptionId);
                    return prescriptionDAO.addPrescription(newPrescription);
                });
    }
}