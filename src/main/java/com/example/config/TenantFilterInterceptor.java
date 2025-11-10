//package com.example.config;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.hibernate.Session;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class TenantFilterInterceptor extends OncePerRequestFilter {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String tenantId = request.getHeader("tenantId"); // Or from JWT
//        if (tenantId != null) {
//            TenantContext.setTenantId(tenantId);
//            Session session = entityManager.unwrap(Session.class);
//            session.enableFilter("tenantFilter")
//                    .setParameter("tenantId", tenantId);
//            logger.info("✅ Tenant filter enabled for tenant: {}");
//        }
//
//        try {
//            filterChain.doFilter(request, response);
//        } finally {
//            TenantContext.clear();
//            logger.info("✅ TenantContext cleared after request");
//        }
//    }
//}
