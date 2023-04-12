package com.daily.log.login;

import com.daily.log.jwt.JwtTokenProvider;
import com.daily.log.jwt.RefreshTokenRepository;
import com.daily.log.team.TeamResponseDto;
import com.daily.log.team.TeamService;
import com.daily.log.tenant.TenantResponseDto;
import com.daily.log.tenant.TenantService;
import com.daily.log.userAuth.UserAuthRequestDto;
import com.daily.log.userAuth.UserAuthService;
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final MyUserDetailService myUserDetailService;
    private final UserInfoService userInfoService;
    private final TenantService tenantService;
    private final TeamService teamService;
    private final UserAuthService userAuthService;

    final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository repository; // 삭제

    @Transactional
    public void signUp(UserInfoRequestDto userInfoRequestDto,Map<String,Object> result){ // 회원가입
        try {
            if(userInfoService.getUserById(userInfoRequestDto.getUserId()) != null){
                result.put("success",false);
                result.put("msg","이미 가입된 아이디입니다.");
                return;
            }
        } catch (RuntimeException e){
        }
        if(userInfoRequestDto.getUserPwd().isEmpty()){
            result.put("success",false);
            result.put("msg","비밀번호를 입력하여 주십시오.");
            return;
        } else {
            if(!userInfoRequestDto.getAuthNum().equals(userAuthService.getUserAuthByUsrId(userInfoRequestDto.getUserId()).getAuthNum())){
                result.put("success",false);
                result.put("msg","인증번호를 잘 못 입력하였습니다.");
                return;
            } else {
                if(userInfoRequestDto.getUserName().isEmpty()){
                    result.put("success",false);
                    result.put("msg","이름을 입력하여 주십시오.");
                    return;
                }
            }
        }
        userInfoService.createUser(userInfoRequestDto);
        result.put("success",true);
        result.put("msg","회원가입이 완료되었습니다.");
    }

    @Transactional()
    public SignInResponseDto signIn(UserInfoRequestDto userInfoRequestDto, Map<String,Object> result) {
        UserDetails userDetails = null;

        try {
            userDetails = myUserDetailService.loadUserByUsername(userInfoRequestDto.getUserId());
        } catch (RuntimeException e){
            if(e.getMessage().equals("User not found")){
                result.put("success",false);
                result.put("msg","존재하지 않는 사용자입니다.");
            } else {
                result.put("success",false);
                result.put("msg","로그인 중 오류가 발생했습니다.");
            }
        }

        if(userDetails == null){
            result.put("success",false);
            result.put("msg","존재하지 않는 사용자입니다.");
            return null;
        }

        if(!passwordEncoder.matches(userInfoRequestDto.getUserId() + ":" + userInfoRequestDto.getUserPwd(), userDetails.getPassword())){
            result.put("success",false);
            result.put("msg","비밀번호가 틀렸습니다.");
            return null;
        }

        Authentication authentication =  new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

        log.info("signIn service | authentication.getName : {}, authentication.getCredentials() : {}",
                authentication.getName(), authentication.getCredentials());

        return new SignInResponseDto("Bearer-"+jwtTokenProvider.issueRefreshToken(authentication));
    }

    public TenantResponseDto getSignUpInitData(String clientUrl){
        TenantResponseDto tenantResponseDto = tenantService.getTenantDtoByTenantUrl(clientUrl);
        List<TeamResponseDto> teamList = teamService.getTeamsByTeamTenantSeq(tenantResponseDto.getTenantSeq());
        tenantResponseDto.setTeamList(teamList);
        return tenantResponseDto;
    }

    public String createUserAuthNum(String userId){
        String result = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        userAuthService.createUserAuth(new UserAuthRequestDto(userId, result));
        return result;
    }

    public void checkExist(String userId, Map<String,Object> result){ // 회원가입
        try {
            if(userInfoService.getUserById(userId) != null){
                result.put("success",false);
            } else {
                result.put("success",true);
            }
        } catch (RuntimeException e){
            if(e.getMessage().equals("User not found")){
                result.put("success",true);
            }
        }
        // 중복체크
    }
}