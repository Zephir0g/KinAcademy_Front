package com.fo4ik.kinacademy.restControllers;

//TODO change this class name for {"/search" and other}

import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.dto.course.CourseDto;
import com.fo4ik.kinacademy.dto.course.SingUpCourseDto;
import com.fo4ik.kinacademy.entity.course.Course;
import com.fo4ik.kinacademy.entity.data.service.CourseService;
import com.fo4ik.kinacademy.entity.data.service.UserService;
import com.fo4ik.kinacademy.exceptions.AppException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/course")
@RestController
@SecurityRequirement(name = "Bearer token")
@Tag(name = "Courses", description = "Methods for working with courses")
public class CoursesController {

    private final UserService userService;
    private final CourseService courseService;

    @Operation(summary = "Search courses", tags = {"Courses"})
    @GetMapping("/search")
    public List<Course> search(@RequestParam String query) {
        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder().id(1L).name("Course 1").category("Category 1").build());
        courses.add(Course.builder().id(2L).name("Course 2").category("Category 2").build());

        List<Course> answer = new ArrayList<>();

        return courses;
    }

    @Operation(summary = "Create course", description = "Create course by using CourseDto", tags = {"Courses"})
    @PostMapping("/create/{id}")
    public ResponseEntity<CourseDto> createCourse(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data for course, see SignUpCourseDto", required = true)
            @RequestBody SingUpCourseDto signUpCourseDto,
            @Parameter(description = "User id", required = true)
            @PathVariable Long id,
            @Parameter(hidden = true)
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String SECURE_TOKEN
    ) {

        Response response = userService.isUserValid(SECURE_TOKEN, id);
        if(!response.isSuccess()){
            throw new AppException(response.getMessage(), response.getHttpStatus());
        }

        CourseDto course = courseService.createCourse(signUpCourseDto, id);
        return ResponseEntity.ok(course);
    }
}
