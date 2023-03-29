package com.daily.log.jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "refreshToken";

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveAccessToken(request, AUTHORIZATION_HEADER);

        if (jwt != null && jwtTokenProvider.validateToken(jwt)== JwtTokenProvider.JwtCode.ACCESS) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("set Authentication to security context for '{}', uri: {}", authentication.getName(), request.getRequestURI());
        }
        else if( jwt != null && jwtTokenProvider.validateToken(jwt) == JwtTokenProvider.JwtCode.EXPIRED){
            String refresh = resolveToken(request,REFRESH_HEADER);
            // refresh token을 확인해서 재발급해준다
            if(refresh != null && jwtTokenProvider.validateToken(refresh) == JwtTokenProvider.JwtCode.ACCESS){
                String newRefresh = jwtTokenProvider.reissueRefreshToken(refresh);
                if(newRefresh != null){
                    ResponseCookie cookie = ResponseCookie.from("refreshToken", "Bearer-"+newRefresh)
                            .maxAge(7 * 24 * 60 * 60)
                            .path("/")
                            .secure(false)
                            .sameSite("None")
                            .httpOnly(true)
                            .build();
                    response.setHeader("Set-Cookie", cookie.toString());

                    // access token 생성
                    Authentication authentication = jwtTokenProvider.getAuthentication(refresh);
                    response.setHeader(AUTHORIZATION_HEADER, "Bearer-"+jwtTokenProvider.createAccessToken(authentication));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("reissue refresh Token & access Token");
                }
            } else {
                log.info("no valid Refresh JWT token found, uri: {}", request.getRequestURI());
                throw new JwtException("유효하지 않은 refreshToken");
            }
        }
        else {
            log.info("no valid JWT token found, uri: {}", request.getRequestURI());
            throw new JwtException("유효하지 않은 accessToken");
        }

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

    private String resolveAccessToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (bearerToken != null && bearerToken.startsWith("Bearer-")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
