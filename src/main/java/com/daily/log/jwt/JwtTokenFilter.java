package com.daily.log.jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "refreshToken";

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return request.getRequestURI().startsWith("/login/");
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getRequestURI().startsWith("/login/")){
            String refresh = resolveToken(request,REFRESH_HEADER);
            // refresh token을 확인해서 재발급해준다
            if(refresh != null && jwtTokenProvider.validateToken(refresh) == JwtTokenProvider.JwtCode.ACCESS){
                String newRefresh = jwtTokenProvider.reissueRefreshToken(refresh);
                if(newRefresh != null){

                    Cookie httpCookie = new Cookie("refreshToken", "Bearer-"+newRefresh);
                    httpCookie.setMaxAge(7 * 24 * 60 * 60);
                    httpCookie.setPath("/");
                    httpCookie.setHttpOnly(true);

                    Authentication authentication = jwtTokenProvider.getAuthentication(newRefresh);

                    Cookie isLoginCookie = new Cookie("isLogin", URLEncoder.encode(authentication.getName()+"isLogin"));
                    isLoginCookie.setMaxAge(7 * 24 * 60 * 60);
                    isLoginCookie.setPath("/");
                    isLoginCookie.setHttpOnly(false);

                    response.addCookie(isLoginCookie);
                    response.addCookie(httpCookie);

                    request.setAttribute("loginUser",authentication.getName());
                    log.info("reissue refresh Token & access Token");
                } else {
                    log.info("no valid Refresh JWT token found, uri: {}", request.getRequestURI());
                    request = new HttpServletRequestWrapper((HttpServletRequest) request) {
                        @Override
                        public String getRequestURI() {
                            return "/login/logOut";
                        }
                    };
                }
            } else {
                log.info("no valid Refresh JWT token found, uri: {}", request.getRequestURI());
                request = new HttpServletRequestWrapper((HttpServletRequest) request) {
                    @Override
                    public String getRequestURI() {
                        return "/login/logOut";
                    }
                };
            }
        }
        response.setHeader("Access-Control-Allow-Origin", request.getHeader(HttpHeaders.ORIGIN));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept, Set-Cookie");
        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request, String header) {
        Cookie[] list = request.getCookies();
        if(list != null) {
            for (Cookie cookie : list) {
                if (cookie.getName().equals(header)) {
                    String bearerToken = cookie.getValue();
                    logger.info(bearerToken);
                    if (bearerToken != null && bearerToken.startsWith("Bearer-")) {
                        return bearerToken.substring(7);
                    }
                }
            }
        }
        return null;
    }
}
