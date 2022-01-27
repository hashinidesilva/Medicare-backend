package com.hashini.medicare.dto;

public class PrescriptionMedicineDTO {

    private MedicineDTO medicine;
    private String dose;
    private String frequency;
    private String duration;
    private String additionalInfo;
    private int quantity;

    public PrescriptionMedicineDTO(MedicineDTO medicine,
                                   String dose,
                                   String frequency,
                                   String duration,
                                   String additionalInfo,
                                   int quantity) {
        this.medicine = medicine;
        this.dose = dose;
        this.frequency = frequency;
        this.duration = duration;
        this.additionalInfo = additionalInfo;
        this.quantity = quantity;
    }

    public MedicineDTO getMedicine() {
        return medicine;
    }

    public void setMedicine(MedicineDTO medicine) {
        this.medicine = medicine;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
