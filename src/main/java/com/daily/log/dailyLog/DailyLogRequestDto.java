package com.daily.log.dailyLog;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogRequestDto {

    private Long logSeq;

    @NotBlank(message = "제목을 입력해주세요.")
    private String logTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String logContent;

    private String userId;

    private String logDate;

    private Date setStartTime;

    private Date setEndTime;

    private Date startTime;

    private Date endTime;

    private String delYn;

    public DailyLog toEntity(){
        return new DailyLog(logSeq,logTitle,logContent,userId,logDate,setStartTime,setEndTime,startTime,endTime,delYn,null);
    }
}