package com.daily.log.dailyLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {
    List<DailyLog> findDailyLogsByUserIdAndLogDateAndDelYnOrderBySetStartTime(String userId, String logDate, String delYn);
}
