package com.opd.opd_ai_system.controller;

import com.opd.opd_ai_system.dto.AnnualReportDTO;
import com.opd.opd_ai_system.dto.MonthlyReportDTO;
import com.opd.opd_ai_system.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/monthly")
    public MonthlyReportDTO getMonthlyReport(

            @RequestParam int year,
            @RequestParam int month

    ){

        return reportService.getMonthlyReport(
                year,
                month
        );

    }
    @GetMapping("/annual")
    public AnnualReportDTO getAnnualReport(
            @RequestParam int year){

        return reportService.getAnnualReport(year);

    }

}