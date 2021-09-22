package com.example.util;

import java.util.Arrays;
import java.util.List;

import com.example.demo.student.Student;

public final class StudentHelper {
    
    public static final List<Student> STUDENTS = Arrays.asList(
        new Student(1, "James Bond"),
        new Student(2, "Maria Jones"),
        new Student(3, "Anna Smith"),
        new Student(4, "Jose Miko")
    );

    private StudentHelper() {
        
    }

}