package com.daily.log.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantService {

    final TenantRepository tenantRepository;

    public TenantResponseDto getTenantDtoByTenantUrl(String tenantUrl) {
        Tenant tenant = tenantRepository.findFirstByTenantUrl(tenantUrl);
        return new TenantResponseDto(tenant);
    }

    public TenantResponseDto getTenantDtoByTenantSeq(Long tenantSeq) {
        Tenant tenant = tenantRepository.findById(tenantSeq).orElseThrow(() -> new RuntimeException("Tenant not found"));;
        return new TenantResponseDto(tenant);
    }
}