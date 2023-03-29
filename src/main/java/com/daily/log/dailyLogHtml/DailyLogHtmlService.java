package com.daily.log.dailyLogHtml;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyLogHtmlService {
    final DailyLogHtmlRepository dailyLogHtmlRepository;

    public DailyLogHtmlResponseDto getDailyLogHtmlById(Long logSeq) {
        DailyLogHtml dailyLogHtml = dailyLogHtmlRepository.findById(logSeq)
                .orElseThrow(() -> new RuntimeException("DailyLogHtml not found"));
        return new DailyLogHtmlResponseDto(dailyLogHtml);
    }
}
