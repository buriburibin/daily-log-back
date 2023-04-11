package com.daily.log.dailyLogComment;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class DailyLogCommentRequestDto {

    private Long logSeq;

    @NotBlank(message = "코멘트 내용을 입력해주세요.")
    private String commentContent;

    private String userId;

    public DailyLogComment toEntity(){
        return new DailyLogComment(logSeq,commentContent,userId,"N");
    }
}