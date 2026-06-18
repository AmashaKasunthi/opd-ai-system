package com.opd.opd_ai_system.controller;


import com.opd.opd_ai_system.entity.MedicalRecord;
import com.opd.opd_ai_system.service.MedicalRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@CrossOrigin(origins = "http://localhost:5173")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    // Add medical record
    @PostMapping("/{patientId}/{userId}")
    public MedicalRecord addRecord(
            @PathVariable Integer patientId,
            @PathVariable Integer userId,
            @RequestBody MedicalRecord record){

        return medicalRecordService.addRecord(
                patientId,
                userId,
                record
        );
    }

    // View all records
    @GetMapping
    public List<MedicalRecord> getAllRecords(){
        return medicalRecordService.getAllRecords();
    }

    // View one record
    @GetMapping("/{id}")
    public MedicalRecord getRecordById(
            @PathVariable Integer id){

        return medicalRecordService.getRecordById(id);
    }

    // Update record
    @PutMapping("/{id}")
    public MedicalRecord updateRecord(
            @PathVariable Integer id,
            @RequestBody MedicalRecord record){

        return medicalRecordService.updateRecord(id, record);
    }

    // Delete record
    @DeleteMapping("/{id}")
    public String deleteRecord(
            @PathVariable Integer id){

        return medicalRecordService.deleteRecord(id);
    }
}