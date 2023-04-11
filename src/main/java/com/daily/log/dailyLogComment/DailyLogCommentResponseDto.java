package com.daily.log.dailyLogComment;

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
public class DailyLogCommentResponseDto {

    private Long commentSeq;

    private String commentContent;

    private String userId;

    private String commentWriter;

    private String regDate;
    private boolean isMine;
    public DailyLogCommentResponseDto(DailyLogComment dailyLogComment){
        this.commentSeq = dailyLogComment.getCommentSeq();
        this.commentContent = dailyLogComment.getCommentContent();
        this.userId = dailyLogComment.getUserId();
        this.regDate = dailyLogComment.getRegDate().toLocaleString();
    }
}