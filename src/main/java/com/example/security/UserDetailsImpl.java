//package com.example.security;
//
//import com.example.model.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//
//public class UserDetailsImpl implements UserDetails {
//
//    private final String username;
//    private final String password;
//    private final String tenantId;
//    private final String role;
//
//    public UserDetailsImpl(User user) {
//        this.username = user.getUsername();
//        this.password = user.getPassword();
//        this.tenantId = user.getTenantId();
//        this.role = user.getRole().name();
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(() -> "ROLE_" + role);
//    }
//
//    @Override public String getPassword() { return password; }
//    @Override public String getUsername() { return username; }
//
//    public String getTenantId() { return tenantId; }
//    public String getRole() { return role; }
//
//    @Override public boolean isAccountNonExpired() { return true; }
//    @Override public boolean isAccountNonLocked() { return true; }
//    @Override public boolean isCredentialsNonExpired() { return true; }
//    @Override public boolean isEnabled() { return true; }
//}
