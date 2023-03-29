package com.daily.log.dailyLog;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogResponseDto {

    private Long logSeq;

    private String logTitle;

    private String logContent;

    private String userId;

    private String logDate;

    private Date setStartTime;

    private Date setEndTime;

    private Date startTime;

    private Date endTime;

    public DailyLogResponseDto(DailyLog dailyLog){
        this.logSeq = dailyLog.getLogSeq();
        this.logTitle = dailyLog.getLogTitle();
        this.logContent = dailyLog.getLogContent();
        this.userId = dailyLog.getUserId();
        this.logDate = dailyLog.getLogDate();
        this.setStartTime = dailyLog.getSetStartTime();
        this.setEndTime = dailyLog.getSetEndTime();
        this.startTime = dailyLog.getStartTime();
        this.endTime = dailyLog.getEndTime();
    }
}