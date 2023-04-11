package com.daily.log.dailyLog;

import com.daily.log.dailyLogHtml.DailyLogHtml;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogRequestDto {

    private Long logSeq;

    @NotBlank(message = "제목을 입력해주세요.")
    private String logTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String logContent;

    private String logHtml;

    private String userId;

    private String logDate;

    private String setStartTime;

    private String setEndTime;

    private String startTime;

    private String endTime;

    private String delYn;

    public DailyLog toEntity(String delYn){
        return new DailyLog(logSeq,logTitle,logContent,userId,logDate,setStartTime,setEndTime,startTime,endTime,delYn);
    }

    public DailyLogHtml toHtmlEntity(){
        return new DailyLogHtml(logSeq,logHtml);
    }
}