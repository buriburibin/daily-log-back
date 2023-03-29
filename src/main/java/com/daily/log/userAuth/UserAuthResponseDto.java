package com.daily.log.userAuth;

import com.daily.log.userInfo.UserInfo;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponseDto {
    private String userId;
    private String authNum;

    public UserAuthResponseDto(UserAuth userAuth){
        this.userId = userAuth.getUserId();
        this.authNum = userAuth.getAuthNum();
    }
}