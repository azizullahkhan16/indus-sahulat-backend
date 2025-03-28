package com.aktic.indussahulatbackend.security;

import com.aktic.indussahulatbackend.model.common.UserBase;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class UserPrincipal implements UserDetails {
    private final UserBase user;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public static UserPrincipal create(UserBase user) {
        String userRole = user.getRole().getRoleName();
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userRole));
        return new UserPrincipal(user, authorities);
    }

    public static UserPrincipal create(UserBase user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getUsername() {
        return user.getPhone();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
