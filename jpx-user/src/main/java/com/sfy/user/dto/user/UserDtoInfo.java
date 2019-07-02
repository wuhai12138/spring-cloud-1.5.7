package com.sfy.user.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by 金鹏祥 on 2019/5/13.
 */
@ApiModel("用户信息")
@Data
public class UserDtoInfo extends UserDto implements UserDetails {

    private static final long serialVersionUID = 1L;
    /***
     * 权限重写
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new HashSet<>();
        if (!CollectionUtils.isEmpty(roles)) {
            roles.parallelStream().forEach(role -> {
                if (role.getValue().startsWith("ROLE_")) {
                    collection.add(new SimpleGrantedAuthority(role.getValue()));
                } else {
                    collection.add(new SimpleGrantedAuthority("ROLE_" + role.getValue()));
                }
            });
        }
        if (!CollectionUtils.isEmpty(permissions)) {
            permissions.parallelStream().forEach(authority -> {
                collection.add(new SimpleGrantedAuthority(authority.getValue()));
            });
        }
        return collection;
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

    @Override
    public boolean isEnabled() {

        if (getEnabled() == null || getEnabled() == 0) {
            return false;
        }
        return true;
    }
}
