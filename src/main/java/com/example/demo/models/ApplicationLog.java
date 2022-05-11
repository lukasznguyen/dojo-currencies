package com.example.demo.models;

import com.example.demo.models.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class ApplicationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime timeStamp;

    @Enumerated(EnumType.STRING)
    private ActionType applicationAction;

    private String requestParams;

    private String requestBody;

}
