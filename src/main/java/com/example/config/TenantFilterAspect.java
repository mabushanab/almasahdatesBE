//package com.example.config;
//
//import jakarta.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.hibernate.Session;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//public class TenantFilterAspect {
//
//    private final EntityManager entityManager;
//
//    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
//    public Object applyTenantFilter(ProceedingJoinPoint pjp) throws Throwable {
//        String tenantId = TenantContext.getTenantId();
//        if (tenantId != null) {
//            Session session = entityManager.unwrap(Session.class);
//            if (session.getEnabledFilter("tenantFilter") == null) {
//                session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
//            }
//        }
//        return pjp.proceed();
//    }
//}
//
