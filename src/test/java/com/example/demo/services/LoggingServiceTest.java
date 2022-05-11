package com.example.demo.services;

import com.example.demo.models.ApplicationLog;
import com.example.demo.models.enums.ActionType;
import com.example.demo.repositiories.ApplicationLogRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoggingServiceTest {

    @Mock
    private ApplicationLogRepo applicationLogRepo;

    @InjectMocks
    private LoggingService underTest;

    @Captor
    private ArgumentCaptor<ApplicationLog> argumentCaptor;

    @Test
    void createDatabaseLogShouldSaveProperApplicationLogData() {
        //given
        ActionType actionType = ActionType.GET_ACTUAL_RATE_FOR_CURRENCY;
        String requestParam = "CustomRequestParam";

        //when
        underTest.createDatabaseLog(actionType, requestParam, null);

        //then
        verify(applicationLogRepo).save(argumentCaptor.capture());
        assertAll(
                () -> assertThat(argumentCaptor.getValue().getTimeStamp(), is(notNullValue())),
                () -> assertThat(argumentCaptor.getValue().getApplicationAction(), equalTo(actionType)),
                () -> assertThat(argumentCaptor.getValue().getRequestParams(), equalTo(requestParam)),
                () -> assertThat(argumentCaptor.getValue().getRequestBody(), is(nullValue()))
        );
    }
}