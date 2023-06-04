package com.cataas.cataasapi.controller;

import com.cataas.cataasapi.service.CatAasApiReportService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CatAasApiReportController {
    private final CatAasApiReportService reportService;

    public CatAasApiReportController(CatAasApiReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/generateReport")
    public  ResponseEntity<Resource> generateReport() throws IOException {
        return reportService.generateReport();

    }
}
