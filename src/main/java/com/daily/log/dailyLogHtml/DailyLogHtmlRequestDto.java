package com.daily.log.dailyLogHtml;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogHtmlRequestDto {

    private Long logSeq;

    private String logHtml;

    public DailyLogHtml toEntity(){
        return new DailyLogHtml(logSeq,logHtml);
    }
}