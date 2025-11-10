package com.example.service;

import com.example.config.TenantContext;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantServiceHelper {
    private final EntityManager entityManager;

    public void enableTenantFilter() {
        String tenantId = TenantContext.getTenantId();
        System.out.println("helper " + tenantId);
        if (tenantId != null) {
            Session session = entityManager.unwrap(Session.class);
            if (session.getEnabledFilter("tenantFilter") == null) {
                session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
            }
        }
    }
}
