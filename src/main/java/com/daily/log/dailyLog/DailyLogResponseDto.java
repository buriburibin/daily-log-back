package com.daily.log.dailyLog;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogResponseDto {

    private Long logSeq;

    private String logTitle;

    private String logContent;

    private String logHtml;

    private String userId;

    private String logWriter;

    private String delYn;

    private String logDate;

    private String setStartTime;

    private String setEndTime;

    private String startTime;

    private String endTime;

    private boolean isTenant;

    private boolean isMine;

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
        this.delYn = dailyLog.getDelYn();
    }
}