package com.daily.log.userInfo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequestDto {
    @Email(message = "이메일 형식에 맞춰 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String userId;
    @Pattern(regexp="[a-zA-Z1-9]{6,12}", message = "비밀번호는 영어와 숫자로 포함해서 6~12자리 이내로 입력해주세요.")
    private String userPwd;
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 8, message = "이름을 2~8자 사이로 입력해주세요.")
    private String userName;
    private Long userTeamSeq;

    public UserInfo toEntity(PasswordEncoder bCryptPasswordEncoder){
        return new UserInfo(userId, bCryptPasswordEncoder.encode(userId + ":" + userPwd),userName,userTeamSeq);
    }
}