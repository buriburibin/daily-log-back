package com.daily.log.dailyLogComment;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reg_date")
    private Date regDate;

    public DailyLogComment(Long logSeq, String commentContent, String userId, String delYn) {
        this.logSeq = logSeq;
        this.commentContent = commentContent;
        this.userId = userId;
        this.delYn = delYn;
    }
}