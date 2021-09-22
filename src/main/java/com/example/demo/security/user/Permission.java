package com.example.demo.security.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Permission {

    STUDENT_READ("student_read"),
    STUDENT_WRITE("student_write"),
    COURSE_READ("course_read"),
    COURSE_WRITE("course_write");

    @Getter
    private final String permission;

}
