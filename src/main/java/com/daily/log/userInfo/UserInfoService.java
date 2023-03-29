package com.daily.log.userInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    final PasswordEncoder passwordEncoder;
    final UserInfoRepository userInfoRepository;

    public List<UserInfoResponseDto> getUsersByTeamSeq(Long teamSeq) {
        return userInfoRepository.findUsersByUserTeamSeq(teamSeq).stream()
                .map(m->new UserInfoResponseDto(m))
                .collect(Collectors.toList());
    }

    public UserInfoResponseDto getUserById(String userId) {
        UserInfo userInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserInfoResponseDto(userInfo);
    }

    public UserInfoResponseDto createUser(UserInfoRequestDto userInfoRequestDto) {
        UserInfo userInfo = userInfoRepository.save(userInfoRequestDto.toEntity(passwordEncoder));
        return new UserInfoResponseDto(userInfo);
    }
}
