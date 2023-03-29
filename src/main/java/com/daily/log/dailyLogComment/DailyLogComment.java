package com.daily.log.dailyLogComment;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "daily_log_comment")
public class DailyLogComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    private Long commentSeq;

    @Column(name = "log_seq", nullable = false)
    private Long logSeq;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "del_yn")
    private String delYn;

    @Column(name = "reg_date")
    private Date regDate;
}