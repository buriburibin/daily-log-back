package com.daily.log.dailyLogHtml;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogHtmlResponseDto {

    private String logHtml;

    public DailyLogHtmlResponseDto(DailyLogHtml dailyLogHtml){
        this.logHtml = dailyLogHtml.getLogHtml();
    }
}