package com.daily.log.tenant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TenantResponseDto {
    private Long tenantSeq;

    private String tenantName;

    private String tenantUrl;

    private String tenantHost;

    public TenantResponseDto(Tenant tenant){
        this.tenantSeq = tenant.getTenantSeq();
        this.tenantName = tenant.getTenantName();
        this.tenantUrl = tenant.getTenantUrl();
        this.tenantHost = tenant.getTenantHost();
    }
}