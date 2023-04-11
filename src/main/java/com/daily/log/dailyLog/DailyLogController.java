package com.daily.log.dailyLog;

import com.daily.log.dailyLogComment.DailyLogCommentRequestDto;
import com.daily.log.dailyLogComment.DailyLogCommentResponseDto;
import com.daily.log.dailyLogComment.DailyLogCommentService;
import com.daily.log.team.TeamResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DailyLogController {

    final DailyLogService dailyLogService;
    final DailyLogCommentService dailyLogCommentService;

    @PostMapping("/user/dailyLog/{date}/list")
    public ResponseEntity<List<DailyLogResponseDto>> myLogList(HttpServletRequest request, @PathVariable String date){
        List<DailyLogResponseDto> dailyLogList = dailyLogService.getDailyLogsByUserIdAndLogDateAndDelYnOrderBySetStartTime((String) request.getAttribute("loginUser"),date,"N");
        return ResponseEntity.ok(dailyLogList);
    }

    @PutMapping("/user/dailyLog")
    public ResponseEntity<DailyLogResponseDto> registerLog(HttpServletRequest request, @RequestBody DailyLogRequestDto dailyLogRequestDto){
        dailyLogRequestDto.setUserId((String) request.getAttribute("loginUser"));
        DailyLogResponseDto dailyLogList = dailyLogService.saveDailyLog(dailyLogRequestDto);
        return ResponseEntity.ok(dailyLogList);
    }

    @GetMapping("/dailyLog/{logSeq}")
    public ResponseEntity<DailyLogResponseDto> getDailyLogDetail(HttpServletRequest request, @PathVariable Long logSeq){
        DailyLogResponseDto dailyLogResponseDto = dailyLogService.getDailyLogById(logSeq, (String) request.getAttribute("loginUser"));
        return ResponseEntity.ok(dailyLogResponseDto);
    }

    @GetMapping("/tenant/dept/user/dailyLog/{date}/list")
    public ResponseEntity<List<TeamResponseDto>> getTenantDailyLogList(HttpServletRequest request, @PathVariable String date){
        return ResponseEntity.ok(dailyLogService.getTenantDailyLogsByUserId((String) request.getAttribute("loginUser"),date,"N"));
    }

    @DeleteMapping("/dailyLog/{logSeq}")
    public ResponseEntity<Map<String,Object>> deleteDailyLog(HttpServletRequest request, @PathVariable Long logSeq){
        Map<String,Object> result = new HashMap<>();
        dailyLogService.deleteDailyLogById(logSeq, (String) request.getAttribute("loginUser"),result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/user/dailyLog/{logSeq}/operationTime/{type}")
    public ResponseEntity<Map<String,Object>> setDailyLogOperationType(HttpServletRequest request, @PathVariable Long logSeq, @PathVariable String type){
        Map<String,Object> result = new HashMap<>();
        dailyLogService.setDailyLogOperationTime(logSeq, (String) request.getAttribute("loginUser"), type, result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/dailyLog/{logSeq}/comment")
    public ResponseEntity<List<DailyLogCommentResponseDto>> getDailyLogComment(HttpServletRequest request, @PathVariable Long logSeq){
        List<DailyLogCommentResponseDto> commentList = dailyLogCommentService.getDailyLogCommentsByLogSeqAndDelYnOrderByRegDate(logSeq,"N",(String) request.getAttribute("loginUser"));
        return ResponseEntity.ok(commentList);
    }

    @PutMapping("/dailyLog/{logSeq}/comment")
    public ResponseEntity<List<DailyLogCommentResponseDto>> registerDailyLogComment(HttpServletRequest request, @PathVariable Long logSeq, @RequestBody String commentContent){
        DailyLogCommentRequestDto dailyLogCommentRequestDto = new DailyLogCommentRequestDto(logSeq,commentContent,(String) request.getAttribute("loginUser"));
        List<DailyLogCommentResponseDto> commentList = dailyLogCommentService.saveDailyLogComment(dailyLogCommentRequestDto, (String) request.getAttribute("loginUser"));
        return ResponseEntity.ok(commentList);
    }

    @DeleteMapping("/dailyLog/{logSeq}/comment/{commentSeq}")
    public ResponseEntity<Map<String,Object>> deleteDailyLogComment(HttpServletRequest request, @PathVariable Long logSeq, @PathVariable Long commentSeq){
        Map<String,Object> result = new HashMap<>();
        dailyLogCommentService.deleteDailyLogComment(result, logSeq, commentSeq, (String) request.getAttribute("loginUser"));
        return ResponseEntity.ok(result);
    }
}
