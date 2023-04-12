package com.daily.log.tenant;

import com.daily.log.team.TeamResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantResponseDto {
    private Long tenantSeq;

    private String tenantName;

    private String tenantUrl;

    private String tenantHost;

    private List<TeamResponseDto> teamList;

    public TenantResponseDto(Tenant tenant){
        this.tenantSeq = tenant.getTenantSeq();
        this.tenantName = tenant.getTenantName();
        this.tenantUrl = tenant.getTenantUrl();
        this.tenantHost = tenant.getTenantHost();
    }
}