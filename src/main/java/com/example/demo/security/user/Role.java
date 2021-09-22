package com.example.demo.security.user;

import static com.example.demo.security.user.Permission.*;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor
public enum Role {

    STUDENT(Sets.newHashSet(STUDENT_READ)),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),  
    ADMINTRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));

    @Getter
    private final Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> localPermissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        localPermissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return localPermissions;
    }

}