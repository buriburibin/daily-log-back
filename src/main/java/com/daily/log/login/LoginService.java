package com.daily.log.login;

import com.daily.log.jwt.JwtTokenProvider;
import com.daily.log.jwt.RefreshTokenRepository;
import com.daily.log.userInfo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final MyUserDetailService myUserDetailService;
    private final UserInfoService userInfoService;

    final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository repository; // 삭제

    @Transactional
    public UserInfoResponseDto signUp(UserInfoRequestDto userInfoRequestDto){ // 회원가입
        // 중복체크
        validateDuplicateUser(userInfoRequestDto.getUserId());
        return userInfoService.createUser(userInfoRequestDto);
    }

    @Transactional()
    public SignInResponseDto signIn(UserInfoRequestDto userInfoRequestDto) {
        UserDetails userDetails = myUserDetailService.loadUserByUsername(userInfoRequestDto.getUserId());

        if(!passwordEncoder.matches(userInfoRequestDto.getUserId() + ":" + userInfoRequestDto.getUserPwd(), userDetails.getPassword())){
            throw new BadCredentialsException(userDetails.getUsername() + "Invalid password");
        }

        Authentication authentication =  new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

        log.info("signIn service | authentication.getName : {}, authentication.getCredentials() : {}",
                authentication.getName(), authentication.getCredentials());

        return new SignInResponseDto("Bearer-"+jwtTokenProvider.issueRefreshToken(authentication));
    }

    private void validateDuplicateUser(String userId){
        try {
            if(userInfoService.getUserById(userId)!=null){
                log.debug("userId : {}, 아이디 중복으로 회원가입 실패", userId);
                throw new RuntimeException("아이디 중복");
            }
        } catch (Exception e){
            log.debug("userId : {}, 아이디 존재하지 않음.", userId);
        }
    }
}