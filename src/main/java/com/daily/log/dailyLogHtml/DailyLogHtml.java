package com.daily.log.dailyLogHtml;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "daily_log_html")
public class DailyLogHtml {

    @Id
    @Column(name = "log_seq", nullable = false)
    private Long logSeq;

    @Column(name = "log_html", nullable = false)
    private String logHtml;
}