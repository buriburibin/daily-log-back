package com.daily.log.userInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserInfo member = userInfoRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("userId : " + userId + " was not found"));

        return createUserDetails(member);
    }

    private UserDetails createUserDetails(UserInfo member) {
        List<SimpleGrantedAuthority> grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority("all"));
        return new User(member.getUserId(),member.getUserPwd(),grantedAuthorities);
    }
}