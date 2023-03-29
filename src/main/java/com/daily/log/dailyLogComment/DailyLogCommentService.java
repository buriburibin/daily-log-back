package com.daily.log.dailyLogComment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyLogCommentService {
    final DailyLogCommentRepository dailyLogCommentRepository;

    public List<DailyLogCommentResponseDto> getDailyLogCommentsByLogSeqAndDelYnOrderByRegDate(Long logSeq, String delYn) {
        return dailyLogCommentRepository.findDailyLogCommentsByLogSeqAndDelYnOrderByRegDate(logSeq,delYn).stream()
                .map(m->new DailyLogCommentResponseDto(m))
                .collect(Collectors.toList());
    }

    public DailyLogCommentResponseDto saveDailyLogComment(DailyLogCommentRequestDto dailyLogCommentRequestDto) {
        DailyLogComment dailyLogComment = dailyLogCommentRepository.save(dailyLogCommentRequestDto.toEntity());
        return new DailyLogCommentResponseDto(dailyLogComment);
    }
}
