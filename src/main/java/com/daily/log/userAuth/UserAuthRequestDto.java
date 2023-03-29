package com.daily.log.userAuth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthRequestDto {
    private String userId;

    private String authNum;

    public UserAuth toEntity(){
        return new UserAuth(null,userId,authNum,null);
    }
}