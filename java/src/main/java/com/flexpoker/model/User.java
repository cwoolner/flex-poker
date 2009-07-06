package com.flexpoker.model;

import java.security.Principal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails, Principal {

    private Integer id;

    private String username;

    private String password;
    
    private GrantedAuthority[] grantedAuthorities
            = new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_USER") };

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    @Transient
    public GrantedAuthority[] getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }

    @Override
    @Transient
    public String getName() {
        return username;
    }

}
