package com.auto.report.data.controller;


import com.auto.report.data.dto.DataDto;
import com.auto.report.data.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    @PostMapping("/data/create")
    public ResponseEntity<Void> createData(@RequestBody DataDto data){

        long result;
        try {
            result = dataService.createData(data);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // 성공
        if (result > 0){
            return ResponseEntity.ok().build();
        }

        // 실패
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
