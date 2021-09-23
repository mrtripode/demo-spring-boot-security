package com.example.demo.student;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.demo.exception.StudentNotFoundException;

import static com.example.util.ApiPath.API_MANAGEMENT_STUDENTS;
import static com.example.util.StudentHelper.STUDENTS;


@Slf4j
@RestController()
@RequestMapping(path = API_MANAGEMENT_STUDENTS)
public class StudentManagementController {

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public List<Student> getStudents() {
        return STUDENTS;
    }

    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public ResponseEntity<Student> getStudent(@PathVariable("studentId") Integer studentId) {
        Student student = STUDENTS
                        .stream()
                        .filter(s -> s.getStudentId().equals(studentId))
                        .findFirst()
                        .orElseThrow(() -> new StudentNotFoundException("The student (" + studentId + ") doas not exists"));
        return ResponseEntity.ok(student);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('student_write')")
    public ResponseEntity<Student> registerNewStudent(@RequestBody Student student) {
        Optional<Integer> maxIdOptional = getStudents().stream().map(Student::getStudentId).max(Integer::compare);
        int maxId = 0;

        if (maxIdOptional.isPresent()) {
            maxId = maxIdOptional.get();

            log.info(String.format("New student in the system:: ID=%s, %s", ++maxId, student.getStudentName()));

            Student newStudent = Student.builder()
                                        .studentId(maxId)
                                        .studentName(student.getStudentName())
                                        .build();
            URI location;

            try {
                location = new URI(API_MANAGEMENT_STUDENTS + "/" + maxId);
            } catch (URISyntaxException e) {
                log.error("Error creating the URI loaction", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(location);

            return new ResponseEntity<>(newStudent, responseHeaders, HttpStatus.CREATED);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping(path = "{studentId}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('student_write')")
    public ResponseEntity<Student> updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        final Student oldStudent = getStudent(studentId).getBody();

        if (Objects.isNull(oldStudent)) {
             throw new StudentNotFoundException("The student (" + studentId + ") doas not exists"); 
        }

        final int id = oldStudent.getStudentId();

        log.info(String.format("Before Change:: %s %s", id, oldStudent.getStudentName()));
        log.info(String.format("After Change:: %s %s", id, student.getStudentName()));

        Student newStudent = Student.builder()
                                    .studentId(id)
                                    .studentName(student.getStudentName())
                                    .build();

        return ResponseEntity.accepted().body(newStudent);
    }

    @DeleteMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('student_write')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        log.info(String.format("Erased of the system the user with ID: %s", studentId));
    }

}
