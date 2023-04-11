package com.daily.log.team;

import com.daily.log.tenant.Tenant;
import com.daily.log.userInfo.UserInfo;
import com.daily.log.userInfo.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDto {
    private Long teamSeq;
    private Long teamTenantSeq;
    private String teamName;
    private List<UserInfoResponseDto> userInfoList;

    public TeamResponseDto(Team team){
        this.teamSeq = team.getTeamSeq();
        this.teamName = team.getTeamName();
        this.teamTenantSeq = team.getTeamTenantSeq();
    }
}