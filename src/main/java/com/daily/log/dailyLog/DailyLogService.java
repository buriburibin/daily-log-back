package com.daily.log.dailyLog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyLogService {
    final DailyLogRepository dailyLogRepository;

    public List<DailyLogResponseDto> getDailyLogsByUserIdAndLogDateAndDelYnOrderBySetStartTime(String userId, String logDate, String delYn) {
        return dailyLogRepository.findDailyLogsByUserIdAndLogDateAndDelYnOrderBySetStartTime(userId,logDate,delYn).stream()
                .map(m->new DailyLogResponseDto(m))
                .collect(Collectors.toList());
    }

    public DailyLogResponseDto getDailyLogById(Long logSeq) {
        DailyLog dailyLog = dailyLogRepository.findById(logSeq)
                .orElseThrow(() -> new RuntimeException("DailyLog not found"));
        return new DailyLogResponseDto(dailyLog);
    }

    public DailyLogResponseDto saveDailyLog(DailyLogRequestDto dailyLogRequestDto) {
        DailyLog dailyLog = dailyLogRepository.save(dailyLogRequestDto.toEntity());
        return new DailyLogResponseDto(dailyLog);
    }
}
