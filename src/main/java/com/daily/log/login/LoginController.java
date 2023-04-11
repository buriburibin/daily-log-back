package com.daily.log.login;

import com.daily.log.userInfo.UserInfoRequestDto;
import com.daily.log.userInfo.UserInfoResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login/signUp")
    public ResponseEntity<UserInfoResponseDto> signUp(@RequestBody UserInfoRequestDto userInfoRequestDto) {
        return ResponseEntity.ok(loginService.signUp(userInfoRequestDto));
    }

    @PostMapping("/login/signIn")
    public ResponseEntity<Map> signIn(HttpServletResponse response, @RequestBody UserInfoRequestDto userInfoRequestDto) {
        Map<String,Object> result = new HashMap<>();
        result.put("result","success");
        try {
            SignInResponseDto signInResponseDto = loginService.signIn(userInfoRequestDto);
//            ResponseCookie cookie = ResponseCookie.from("refreshToken", signInResponseDto.getRefreshToken())
//                    .maxAge(7 * 24 * 60 * 60)
//                    .path("/")
//                    .secure(false)
//                    .sameSite("None")
//                    .httpOnly(true)
//                    .build();
//            response.setHeader("Set-Cookie", cookie.toString());
//            response.setHeader("Authorization", signInResponseDto.getAccessToken());

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

        } catch (Exception e){
            e.printStackTrace();
            result.put("result","failed");
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
}
