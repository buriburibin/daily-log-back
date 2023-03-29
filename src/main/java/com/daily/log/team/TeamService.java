package com.daily.log.team;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    final TeamRepository teamRepository;

    public List<TeamResponseDto> getTeamsByTeamTenantSeq(Long teamTenantSeq) {
        return teamRepository.findTeamsByTeamTenantSeq(teamTenantSeq).stream()
                .map(m->new TeamResponseDto(m))
                .collect(Collectors.toList());
    }

    public TeamResponseDto getTeamById(Long teamSeq) {
        return new TeamResponseDto(teamRepository.findById(teamSeq).orElseThrow(() -> new RuntimeException("Team not found")));
    }
}
