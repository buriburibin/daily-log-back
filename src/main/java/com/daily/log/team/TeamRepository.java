package com.daily.log.team;

import com.daily.log.tenant.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findTeamsByTeamTenantSeq(Long teamTenantSeq);
}
