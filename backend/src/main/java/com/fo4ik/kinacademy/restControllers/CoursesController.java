package com.fo4ik.kinacademy.restControllers;

//TODO change this class name for {"/search" and other}

import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.core.compress.VideoCompressor;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("api/v1/course")
@RestController
@SecurityRequirement(name = "Bearer token")
@Tag(name = "Courses", description = "Methods for working with courses")
public class CoursesController {

    private final UserService userService;
    private final CourseService courseService;
    private final VideoCompressor videoCompressor;

    @Operation(summary = "Search courses", tags = {"Courses"})
    @GetMapping("/search")
    public List<Course> search(@RequestParam String query) {
        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder().id(1L).name("Course 1").category("Category 1").build());
        courses.add(Course.builder().id(2L).name("Course 2").category("Category 2").build());

        List<Course> answer = new ArrayList<>();

        return courses;
    }

    @Operation(summary = "Create course", description = "Create course by using CourseDto, need SECURE_TOKEN", tags = {"Courses"})
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createCourse(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data for course, see SignUpCourseDto", required = true)
            @RequestBody SingUpCourseDto signUpCourseDto,
            @Parameter(description = "Username", required = true)
            @RequestParam("username") String username
            /*@Parameter(hidden = true)
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String SECURE_TOKEN*/
    ) {

        CourseDto course = courseService.createCourse(signUpCourseDto, username);
        return ResponseEntity.ok(course.getUrl());
    }

    @RequestMapping(value = "/{course-url}", method = RequestMethod.GET, produces = "application/json")
    @Operation(summary = "Get course by url", description = "Get course by url and need SECURE_TOKEN", tags = {"Courses"})
    public ResponseEntity<?> getCourse(
            @Parameter(description = "Course url", required = true)
            @PathVariable("course-url") String courseUrl,
            @Parameter(description = "User username", required = true)
            @RequestParam("username") String username) {


        Response response = courseService.isUserHaveAccessToCourse(username, courseUrl);
        if (!response.isSuccess()) {
            return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
        }
        CourseDto course = courseService.getCourseByUrl(courseUrl);
        return ResponseEntity.ok(course);
    }

    @RequestMapping(value = "/{course-url}/join", method = RequestMethod.POST)
    public ResponseEntity<?> joinCourse(
            @Parameter(description = "Course url", required = true)
            @PathVariable("course-url") String courseUrl,
            @Parameter(description = "User username", required = true)
            @RequestParam("username") String username) {

        Response isUserHaveAccessToCourse = courseService.isUserHaveAccessToCourse(username, courseUrl);

        if (!isUserHaveAccessToCourse.isSuccess()) {
//            return ResponseEntity.status(isUserHaveAccessToCourse.getHttpStatus()).body(isUserHaveAccessToCourse.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have not access to this course");
        }

        Response joinCourse = courseService.joinCourse(username, courseUrl);

        if (!joinCourse.isSuccess()) {
            System.out.println("joinCourse = " + joinCourse.getMessage());
            return ResponseEntity.status(joinCourse.getHttpStatus()).body(joinCourse.getMessage());
        }

        return ResponseEntity.ok(joinCourse.getMessage());
    }

    @RequestMapping(value = "/{course-url}/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateCourse(
            @Parameter(description = "Course url", required = true)
            @PathVariable("course-url") String courseUrl,
            @Parameter(description = "Course data", required = true)
            @RequestBody CourseDto courseDto,
            @Parameter(description = "User username", required = true)
            @RequestParam("username") String username) {

        if (!courseService.isUserIsAuthor(username, courseUrl)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not author of this course");
        }

        Response response = courseService.updateCourse(courseUrl, courseDto);

        if (!response.isSuccess()) {
            return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
        }
        return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
    }


    @RequestMapping(value = "/{course-url}/compress", method = RequestMethod.POST)
    @Operation(summary = "Get course video url", description = "Compress course video by url and need SECURE_TOKEN", tags = {"Courses"})
    public ResponseEntity<Map<String, String>> compressVideo(
            @Parameter(description = "Course url", required = true)
            @PathVariable("course-url") String courseUrl,
            @Parameter(description = "Course video", required = true)
            @RequestParam(value = "video", required = true) MultipartFile video,
            @Parameter(description = "User id", required = true)
            @RequestParam(value = "username", required = true) String username
    ) throws IOException, InterruptedException {

        if (!courseService.isUserIsAuthor(username, courseUrl)) {
            throw new AppException("You are not author of this course", HttpStatus.UNAUTHORIZED);
        }


        String videoPath = VideoCompressor.builder().build().getVideoExtension(video, Path.of("data/" + courseUrl));


        if (videoPath == null) {
            throw new AppException("Video extension is not supported", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new HashMap<>(Map.of("urlToVideo", videoPath.toString())));
    }


    @RequestMapping(value = "/{course-url}/video/{video-url}", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> streamVideo(
            @PathVariable("course-url") String courseUrl,
            @PathVariable("video-url") String videoUrl) throws IOException {

        //TODO check is user have access to this video

        if (videoUrl == null) {
            throw new AppException("Video url is null", HttpStatus.BAD_REQUEST);
        }

        String currentDirectory = System.getProperty("user.dir");

        Path videoPath = Paths.get(currentDirectory + "/data/" + courseUrl + "/" + videoUrl);
        File videoFile = new File(String.valueOf(videoPath));
        if (videoFile.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + videoFile.getName() + "\"")
                    .body(new FileSystemResource(videoFile));
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
