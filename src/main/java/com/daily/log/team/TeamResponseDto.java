package com.daily.log.team;

import com.daily.log.tenant.Tenant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDto {
    private Long teamSeq;

    private String teamName;

    public TeamResponseDto(Team team){
        this.teamSeq = team.getTeamSeq();
        this.teamName = team.getTeamName();
    }
}