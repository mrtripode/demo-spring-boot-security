package com.example.demo.student;

import com.example.demo.exception.StudentNotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.util.ApiPath.API_STUDENTS;
import static com.example.util.StudentHelper.STUDENTS;

@RestController
@RequestMapping(path = API_STUDENTS)
public class StudentController {

    @GetMapping("/{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId) {
        return STUDENTS
                        .stream()
                        .filter(s -> s.getStudentId().equals(studentId))
                        .findFirst()
                        .orElseThrow(() -> new StudentNotFoundException("The student (" + studentId + ") doas not exists"));
    }

}
