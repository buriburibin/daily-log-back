package com.daily.log.userInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
    private String userId;
    private String userName;
    private Long userTeamSeq;

    public UserInfoResponseDto (UserInfo userInfo){
        this.userId = userInfo.getUserId();
        this.userName = userInfo.getUserName();
        this.userTeamSeq = userInfo.getUserTeamSeq();
    }
}