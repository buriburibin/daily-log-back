package com.daily.log.userAuth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    final UserAuthRepository userAuthRepository;

    public UserAuthResponseDto getUserAuthByUsrId(String userId) {
        return new UserAuthResponseDto(userAuthRepository.findFirstByUserIdOrderByRegDateDesc(userId));
    }

    public UserAuthResponseDto createUserAuth(UserAuthRequestDto userAuthRequestDto) {
        return new UserAuthResponseDto(userAuthRepository.save(userAuthRequestDto.toEntity()));
    }
}