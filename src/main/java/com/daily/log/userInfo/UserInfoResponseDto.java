package com.daily.log.userInfo;

import com.daily.log.dailyLog.DailyLogResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
    private String userId;
    private String userName;
    private Long userTeamSeq;
    private List<DailyLogResponseDto> dailyLogList;

    public UserInfoResponseDto (UserInfo userInfo){
        this.userId = userInfo.getUserId();
        this.userName = userInfo.getUserName();
        this.userTeamSeq = userInfo.getUserTeamSeq();
    }
}