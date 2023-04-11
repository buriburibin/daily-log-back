package com.daily.log.userInfo;

import com.daily.log.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    List<UserInfo> findUsersByUserTeamSeqOrderByUserName(Long teamSeq);
}
