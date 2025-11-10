package com.example.config;

import com.example.model.BaseTenantEntity;
import jakarta.persistence.PrePersist;

public class TenantEntityListener {

    @PrePersist
    public void setTenant(BaseTenantEntity entity) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            entity.setTenantId(tenantId);
        }
    }
}
