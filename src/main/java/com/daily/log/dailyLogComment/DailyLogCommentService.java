package com.daily.log.dailyLogComment;

import com.daily.log.dailyLog.DailyLogResponseDto;
import com.daily.log.userInfo.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyLogCommentService {
    final DailyLogCommentRepository dailyLogCommentRepository;
    final UserInfoService userInfoService;

    public List<DailyLogCommentResponseDto> getDailyLogCommentsByLogSeqAndDelYnOrderByRegDate(Long logSeq, String delYn, String userId) {
        return dailyLogCommentRepository.findDailyLogCommentsByLogSeqAndDelYnOrderByRegDate(logSeq,delYn).stream()
                .map(m->
                        {
                            DailyLogCommentResponseDto dailyLogCommentResponseDto = new DailyLogCommentResponseDto(m);
                            dailyLogCommentResponseDto.setCommentWriter(userInfoService.getUserById(dailyLogCommentResponseDto.getUserId()).getUserName());
                            dailyLogCommentResponseDto.setMine(userId.equals(dailyLogCommentResponseDto.getUserId()));
                    return dailyLogCommentResponseDto;
                        }
                )
                .collect(Collectors.toList());
    }

    public List<DailyLogCommentResponseDto> saveDailyLogComment(DailyLogCommentRequestDto dailyLogCommentRequestDto, String userId) {
        dailyLogCommentRepository.save(dailyLogCommentRequestDto.toEntity());
        return getDailyLogCommentsByLogSeqAndDelYnOrderByRegDate(dailyLogCommentRequestDto.getLogSeq(),"N", userId);
    }

    public void deleteDailyLogComment(Map<String,Object> result, Long logSeq, Long commentSeq, String userId) {
        DailyLogComment dailyLogComment = dailyLogCommentRepository.findById(commentSeq).orElseThrow(() -> new RuntimeException("DailyLogComment not found"));
        if(dailyLogComment.getUserId().equals(userId)){
            dailyLogCommentRepository.save(new DailyLogComment(dailyLogComment.getCommentSeq(),dailyLogComment.getLogSeq(),dailyLogComment.getCommentContent(),dailyLogComment.getUserId(),"Y",dailyLogComment.getRegDate()));
            result.put("isMine",true);
        } else {
            result.put("isMine",false);
        }
        result.put("commentList",getDailyLogCommentsByLogSeqAndDelYnOrderByRegDate(logSeq,"N", userId));
    }
}
