package com.daily.log.dailyLogComment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyLogCommentRepository extends JpaRepository<DailyLogComment, Long> {
    List<DailyLogComment> findDailyLogCommentsByLogSeqAndDelYnOrderByRegDate(Long logSeq, String delYn);
}
