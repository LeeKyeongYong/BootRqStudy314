package com.qrstudy.qrstudy.domain.entity.member;

import com.qrstudy.qrstudy.base.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {

    @Column(unique = true)
    private String username;
    @Setter
    private String password;
    @Setter
    private String nickname;
    private String email;

    public boolean isAdmin(){
        return "admin".equals(username);
    }

    public List<? extends GrantedAuthority> getGrantedAuthorities(){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        //모든 멤버는 member권한을 가진다.
        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        //username이 admin이 회원은 추가로 admin권한을 가진다.
        if(isAdmin()){
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }
        return grantedAuthorities;
    }
}
