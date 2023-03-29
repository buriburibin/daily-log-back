package com.daily.log.dailyLogComment;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogCommentRequestDto {

    private Long commentSeq;

    private Long logSeq;

    @NotBlank(message = "코멘트 내용을 입력해주세요.")
    private String commentContent;

    private String userId;

    private String delYn;

    public DailyLogComment toEntity(){
        return new DailyLogComment(commentSeq,logSeq,commentContent,userId,delYn,null);
    }
}