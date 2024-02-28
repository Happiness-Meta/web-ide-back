package org.meta.happiness.webide.common.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.meta.happiness.webide.entity.user.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private User user;
    private Collection<? extends GrantedAuthority> authorities;
    public static UserDetailsImpl from(User user) {
        List<SimpleGrantedAuthority> roles = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.toString())).toList();
        /*//기본적으로 User 권한을 부여
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(Role.ROLE_USER.toString());
        //but Role에 Admin이 포함돼있다면 Admin 권한부여
        if (user.getRoles().contains(Role.ROLE_ADMIN)) {
            simpleGrantedAuthority = new SimpleGrantedAuthority(Role.ROLE_ADMIN.toString());
        }*/

        Collection<GrantedAuthority> collection = new ArrayList<>();
        for (SimpleGrantedAuthority role : roles) {
            collection.add(role);
        }
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.user = user;
        userDetails.authorities = collection;

        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override // 계정의 만료여부
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 계정의 잠김 여부
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override //비밀번호 만료 여부
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // 계정의 활성화 여부
    public boolean isEnabled() {
        return true;
    }
}
