package com.daily.log.dailyLog;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "daily_log")
public class DailyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_seq")
    private Long logSeq;

    @Column(name = "log_title", nullable = false)
    private String logTitle;

    @Column(name = "log_content")
    private String logContent;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "log_date", nullable = false)
    private String logDate;

    @Column(name = "set_start_time", nullable = false)
    private Date setStartTime;

    @Column(name = "set_end_time", nullable = false)
    private Date setEndTime;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "del_yn")
    private String delYn;

    @Column(name = "reg_date")
    private Date regDate;
}