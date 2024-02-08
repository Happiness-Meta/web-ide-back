package org.meta.happiness.webide.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.dto.user.UserRegisterDto;
import org.meta.happiness.webide.entity.BaseTimeEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false)
    private String password;

    private String provider;

    private String refreshToken;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(value = EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    public static User createUser(UserRegisterDto form, String password, String provider) {
        User user = new User();
        user.email = form.getEmail();
        user.nickname = form.getNickname();
        user.password = password;
        user.provider = provider;
        user.roles = Collections.singletonList(Role.ROLE_USER);
        return user;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
    public void addRole(Role role) {
        ArrayList<Role> roles = new ArrayList<>(this.roles);
        roles.add(role);
        this.roles = roles;
    }
    public void changePassword(String password) {
        this.password = password;
    }
    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}