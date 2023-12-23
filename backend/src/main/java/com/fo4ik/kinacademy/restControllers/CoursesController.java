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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
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
    public ResponseEntity<CourseDto> getCourse(
            @Parameter(description = "Course url")
            @PathVariable("course-url") String url) {


        CourseDto course = courseService.getCourseByUrl(url);
        return ResponseEntity.ok(course);
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
            /*@Parameter(hidden = true)
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String SECURE_TOKEN*/) throws IOException {

        Response response = userService.isUserActive(username);
        if (!response.isSuccess()) {
            throw new AppException(response.getMessage(), response.getHttpStatus());
        }
        if (!courseService.isUserIsAuthor(username, courseUrl)) {
            throw new AppException("You are not author of this course", HttpStatus.UNAUTHORIZED);
        }


        Path videoPath = VideoCompressor.builder().build().getVideoExtension(video, Path.of("data/" + courseUrl));


        if (videoPath == null) {
            throw new AppException("Video extension is not supported", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new HashMap<>(Map.of("url", videoPath.toString())));
    }

    @RequestMapping(value = "/{video-url}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getVideoByUrl(@PathVariable("video-url") Path videoUrl) {
        try {

            //TODO check is user have access to this video

            if (videoUrl == null) {
                throw new AppException("Video url is null", HttpStatus.BAD_REQUEST);
            }

            videoUrl = Path.of(videoUrl.toString().replace("=", "/"));
            Path videoPath = Path.of("data/" + videoUrl);

            File file = new File(videoPath.toString());
            if (!file.exists()) {
                throw new AppException("Video not found", HttpStatus.NOT_FOUND);
            }

            Resource videoResource = new UrlResource(videoPath.toUri());

            if (videoResource.exists() && videoResource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(videoResource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
