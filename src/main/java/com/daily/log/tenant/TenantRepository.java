package com.daily.log.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Tenant findFirstByTenantUrl(String tenantUrl);
}
