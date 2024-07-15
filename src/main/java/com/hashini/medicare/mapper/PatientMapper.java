package com.hashini.medicare.mapper;

import com.hashini.medicare.dto.PatientDTO;
import com.hashini.medicare.dto.PrescriptionDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientMapper implements ResultSetExtractor<Map<Long, PatientDTO>> {
    @Override
    public Map<Long, PatientDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, PatientDTO> patientsById = new HashMap<>();
        while (rs.next()) {
            long patientId = rs.getLong("patient_id");
            PatientDTO patient = patientsById.get(patientId);
            if (patient == null) {
                patient = new PatientDTO(rs.getLong("patient_id"),
                        rs.getString("reg_no"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("nic"),
                        rs.getInt("tp_number"),
                        rs.getString("address"),
                        rs.getString("allergies"),
                        rs.getObject("created_date", OffsetDateTime.class),
                        rs.getObject("updated_date", OffsetDateTime.class));
                patientsById.put(patient.getId(), patient);
            }
            List<PrescriptionDTO> prescriptions = patient.getPrescriptions();
            if (prescriptions == null) {
                prescriptions = new ArrayList<>();
                patient.setPrescriptions(prescriptions);
            }
            PrescriptionDTO prescriptionDTO = new PrescriptionDTO(
                    rs.getLong("prescription_id"),
                    null,
                    rs.getObject("prescription_created_date", OffsetDateTime.class),
                    rs.getString("diagnosis"),
                    rs.getString("history"),
                    rs.getBoolean("processed"),
                    rs.getFloat("total_price")
            );
            if (prescriptionDTO.getId() != 0) {
                prescriptions.add(prescriptionDTO);
            }
        }
        return patientsById;
    }
}
