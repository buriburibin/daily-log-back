package com.daily.log.dailyLog;

import com.daily.log.dailyLogHtml.DailyLogHtml;
import com.daily.log.dailyLogHtml.DailyLogHtmlRepository;
import com.daily.log.team.TeamResponseDto;
import com.daily.log.team.TeamService;
import com.daily.log.tenant.TenantService;
import com.daily.log.userInfo.UserInfoResponseDto;
import com.daily.log.userInfo.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyLogService {
    final DailyLogRepository dailyLogRepository;
    final DailyLogHtmlRepository dailyLogHtmlRepository;
    final UserInfoService userInfoService;
    final TenantService tenantService;
    final TeamService teamService;


    public List<DailyLogResponseDto> getDailyLogsByUserIdAndLogDateAndDelYnOrderBySetStartTime(String userId, String logDate, String delYn) {
        UserInfoResponseDto userInfo = userInfoService.getUserById(userId);
        return dailyLogRepository.findDailyLogsByUserIdAndLogDateAndDelYnOrderBySetStartTime(userId,logDate,delYn).stream()
                .map(m->{
                    DailyLogResponseDto dailyLogResponseDto = new DailyLogResponseDto(m);
                    dailyLogResponseDto.setLogWriter(userInfo.getUserName());
                    return dailyLogResponseDto;
                })
                .collect(Collectors.toList());
    }

    public DailyLogResponseDto getDailyLogById(Long logSeq, String userId) {
        DailyLog dailyLog = dailyLogRepository.findById(logSeq)
                .orElseThrow(() -> new RuntimeException("DailyLog not found"));
        DailyLogResponseDto dailyLogResponseDto = new DailyLogResponseDto(dailyLog);
        if(dailyLogResponseDto.getDelYn().equals("Y")){
            dailyLogResponseDto = new DailyLogResponseDto();
            dailyLogResponseDto.setDelYn("Y");
        } else {
            DailyLogHtml dailyLogHtml = dailyLogHtmlRepository.findById(logSeq)
                    .orElseThrow(() -> new RuntimeException("DailyLogHtml not found"));
            dailyLogResponseDto.setLogHtml(dailyLogHtml.getLogHtml());
            UserInfoResponseDto userInfoResponseDto = userInfoService.getUserById(dailyLogResponseDto.getUserId());
            dailyLogResponseDto.setLogWriter(userInfoResponseDto.getUserName());
            if(dailyLogResponseDto.getUserId().equals(userId)){
                dailyLogResponseDto.setMine(true);
                dailyLogResponseDto.setTenant(true);
            } else {
                dailyLogResponseDto.setMine(false);
                if(tenantService.getTenantDtoByTenantSeq(teamService.getTeamById(userInfoResponseDto.getUserTeamSeq()).getTeamTenantSeq()).getTenantSeq() ==
                        tenantService.getTenantDtoByTenantSeq(teamService.getTeamById(userInfoService.getUserById(userId).getUserTeamSeq()).getTeamTenantSeq()).getTenantSeq()){
                    dailyLogResponseDto.setTenant(true);
                } else {
                    dailyLogResponseDto = new DailyLogResponseDto();
                    dailyLogResponseDto.setTenant(false);
                }
            }
        }
        return dailyLogResponseDto;
    }

    public DailyLogResponseDto saveDailyLog(DailyLogRequestDto dailyLogRequestDto) {
        DailyLog dailyLog = dailyLogRepository.save(dailyLogRequestDto.toEntity("N"));
        dailyLogRequestDto.setLogSeq(dailyLog.getLogSeq());
        dailyLogHtmlRepository.save(dailyLogRequestDto.toHtmlEntity());
        return new DailyLogResponseDto(dailyLog);
    }

    public void deleteDailyLogById(Long logSeq, String userId, Map<String,Object> result) {
        DailyLog dailyLog = dailyLogRepository.findById(logSeq)
                .orElseThrow(() -> new RuntimeException("DailyLog not found"));
        if(userId.equals(dailyLog.getUserId())){
            DailyLog deleteDailyLog = new DailyLog(dailyLog.getLogSeq(),dailyLog.getLogTitle(),dailyLog.getLogContent(),dailyLog.getUserId(),dailyLog.getLogDate(),dailyLog.getSetStartTime(),dailyLog.getSetEndTime(),dailyLog.getStartTime(),dailyLog.getEndTime(),"Y");
            dailyLogRepository.save(deleteDailyLog);
            result.put("isMine",true);
        } else {
            result.put("isMine",false);
        }
    }

    public void setDailyLogOperationTime(Long logSeq, String userId, String type, Map<String,Object> result) {
        DailyLog dailyLog = dailyLogRepository.findById(logSeq)
                .orElseThrow(() -> new RuntimeException("DailyLog not found"));
        if(userId.equals(dailyLog.getUserId())){
            DailyLog updateDailyLog = new DailyLog(dailyLog.getLogSeq(),dailyLog.getLogTitle(),dailyLog.getLogContent(),dailyLog.getUserId(),dailyLog.getLogDate(),dailyLog.getSetStartTime(),dailyLog.getSetEndTime(),type.equals("start")? String.valueOf(LocalDateTime.now()):dailyLog.getStartTime(),type.equals("end")? String.valueOf(LocalDateTime.now()):dailyLog.getEndTime(),dailyLog.getDelYn());
            dailyLogRepository.save(updateDailyLog);
            result.put("isMine",true);
        } else {
            result.put("isMine",false);
        }
        result.put("dailyLogList",getDailyLogsByUserIdAndLogDateAndDelYnOrderBySetStartTime(userId,dailyLog.getLogDate(),"N"));
    }

    public List<TeamResponseDto> getTenantDailyLogsByUserId(String userId, String logDate, String delYn) {
        UserInfoResponseDto userInfo = userInfoService.getUserById(userId);
        TeamResponseDto teamResponseDto = teamService.getTeamById(userInfo.getUserTeamSeq());

        List<TeamResponseDto> teamResponseDtoList = teamService.getTeamsByTeamTenantSeq(teamResponseDto.getTeamTenantSeq());

        for(TeamResponseDto teamInfo:teamResponseDtoList){
            List<UserInfoResponseDto> userInfoList = userInfoService.getUsersByTeamSeq(teamInfo.getTeamSeq());
            for(UserInfoResponseDto userInfoResponseDto:userInfoList){
                userInfoResponseDto.setDailyLogList(dailyLogRepository.findDailyLogsByUserIdAndLogDateAndDelYnOrderBySetStartTime(userInfoResponseDto.getUserId(),logDate,delYn).stream()
                        .map(m->{
                            DailyLogResponseDto dailyLogResponseDto = new DailyLogResponseDto(m);
                            dailyLogResponseDto.setLogWriter(userInfoResponseDto.getUserName());
                            return dailyLogResponseDto;
                        })
                        .collect(Collectors.toList()));
            }
            teamInfo.setUserInfoList(userInfoList);
        }

        return teamResponseDtoList;
    }
}
