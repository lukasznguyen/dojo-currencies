package com.example.demo.services;

import com.example.demo.models.ApplicationLog;
import com.example.demo.models.enums.ActionType;
import com.example.demo.repositiories.ApplicationLogRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class LoggingService {

    private final ApplicationLogRepo applicationLogRepo;

    void createDatabaseLog(ActionType actionType, String requestParams, String requestBody) {
        ApplicationLog applicationLog = ApplicationLog.builder()
                .timeStamp(LocalDateTime.now())
                .applicationAction(actionType)
                .requestParams(requestParams)
                .requestBody(requestBody)
                .build();
        applicationLogRepo.save(applicationLog);
        log.info("Saved log into database");
    }
}
