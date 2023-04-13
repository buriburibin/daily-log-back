package com.daily.log.login;

import com.daily.log.tenant.TenantResponseDto;
import com.daily.log.userAuth.UserAuth;
import com.daily.log.userAuth.UserAuthService;
import com.daily.log.userInfo.UserInfoRequestDto;
import com.daily.log.userInfo.UserInfoResponseDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final JavaMailSender javaMailSender;

    private final LoginService loginService;
    private final UserAuthService userAuthService;

    @PostMapping("/login/signUp")
    public ResponseEntity<Map<String,Object>> signUp(@RequestBody UserInfoRequestDto userInfoRequestDto) {
        Map<String,Object> result = new HashMap<>();
        loginService.signUp(userInfoRequestDto,result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login/changeUser")
    public ResponseEntity<Map<String,Object>> changeUser(@RequestBody UserInfoRequestDto userInfoRequestDto) {
        Map<String,Object> result = new HashMap<>();
        loginService.changeUser(userInfoRequestDto,result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login/checkExist")
    public ResponseEntity<Map<String,Object>> checkExist(@RequestBody String userId) {
        Map<String,Object> result = new HashMap<>();
        loginService.checkExist(userId,result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login/checkUser")
    public ResponseEntity<Map<String,Object>> checkUser(@RequestBody String userId) {
        Map<String,Object> result = new HashMap<>();
        loginService.checkUser(userId,result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login/signIn")
    public ResponseEntity<Map<String,Object>> signIn(HttpServletResponse response, @RequestBody UserInfoRequestDto userInfoRequestDto) {
        Map<String,Object> result = new HashMap<>();
        try {
            SignInResponseDto signInResponseDto = loginService.signIn(userInfoRequestDto,result);

            if(signInResponseDto!= null){
                Cookie httpCookie = new Cookie("refreshToken", signInResponseDto.getRefreshToken());
                httpCookie.setMaxAge(7 * 24 * 60 * 60);
                httpCookie.setPath("/");
                httpCookie.setHttpOnly(true);

                Cookie isLoginCookie = new Cookie("isLogin", URLEncoder.encode(userInfoRequestDto.getUserId()+"isLogin"));
                isLoginCookie.setMaxAge(7 * 24 * 60 * 60);
                isLoginCookie.setPath("/");
                isLoginCookie.setHttpOnly(false);

                response.addCookie(isLoginCookie);
                response.addCookie(httpCookie);

                result.put("success",true);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.put("success",false);
            result.put("msg","로그인 중 오류가 발생했습니다.");
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/login/logOut")
    public ResponseEntity<String> test(HttpServletResponse response) {
        Cookie httpCookie = new Cookie("refreshToken", "");
        httpCookie.setMaxAge(0);
        httpCookie.setPath("/");
        httpCookie.setHttpOnly(true);

        Cookie isLoginCookie = new Cookie("isLogin", "");
        isLoginCookie.setMaxAge(0);
        isLoginCookie.setPath("/");
        isLoginCookie.setHttpOnly(false);

        response.addCookie(isLoginCookie);
        response.addCookie(httpCookie);

        return ResponseEntity.ok("logOut");
    }

    @GetMapping("/login/signUp/initData")
    public ResponseEntity<TenantResponseDto> signUpInitData(HttpServletRequest request){
        String clientUrl = request.getHeader("origin");
        return ResponseEntity.ok(loginService.getSignUpInitData(clientUrl));
    }

    @PostMapping("/login/authNumRequest")
    public ResponseEntity<String> authNumRequest(@RequestBody String userId) throws MessagingException {
        String result = loginService.createUserAuthNum(userId);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("스마트캐스트<smartkds79@gmail.com>");
        helper.setTo(userId);
        helper.setSubject("일일업무 인증번호");
        helper.setText("인증번호 : " + result);

        javaMailSender.send(message);
        return ResponseEntity.ok("success");
    }
}
