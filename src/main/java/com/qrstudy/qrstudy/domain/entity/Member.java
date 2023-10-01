package com.qrstudy.qrstudy.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Setter
    private String password;
    @Setter
    private String nickname;

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
