package com.auto.report.report.controller;


import com.auto.report.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/run")
    public ResponseEntity<Void> run() {

        int result = reportService.runReport();
        log.info("Report run result: {}", result);

        switch (result) {
            case 0 :
                return ResponseEntity.badRequest().build();

            case 9999:
                return ResponseEntity.noContent().build();

            default:
                return ResponseEntity.ok().build();
        }
    }
}
