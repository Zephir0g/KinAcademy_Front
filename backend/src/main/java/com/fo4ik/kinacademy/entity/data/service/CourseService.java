package com.fo4ik.kinacademy.entity.data.service;

import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.dto.course.CourseDto;
import com.fo4ik.kinacademy.dto.course.SingUpCourseDto;
import com.fo4ik.kinacademy.entity.course.Course;
import com.fo4ik.kinacademy.entity.data.mappers.CourseMapper;
import com.fo4ik.kinacademy.entity.data.repository.CourseRepository;
import com.fo4ik.kinacademy.entity.user.Status;
import com.fo4ik.kinacademy.entity.user.User;
import com.fo4ik.kinacademy.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserService userService;


    public CourseDto createCourse(SingUpCourseDto singUpCourseDto, String username) {
        Optional<Course> oCourse = courseRepository.findByName(singUpCourseDto.name());

        if (oCourse.isPresent()) {
            throw new AppException("Course already exists", HttpStatus.BAD_REQUEST);
        }


        Course course = courseMapper.singUpCourseDtoToCourse(singUpCourseDto);
        course.setAuthorUsername(username);
        course.setLastUpdateDate(new Date());
        course.setStatus(Status.INACTIVE);

        //if singUpCourseDto.url() is exist in db, throw exception
        Optional<Course> oCourseByUrl = courseRepository.findByUrl(singUpCourseDto.url());
        if (oCourseByUrl.isPresent()) {
            throw new AppException("Course with this url already exists", HttpStatus.BAD_REQUEST);
        } else {
            course.setUrl(singUpCourseDto.url());
        }


        Course savedCourse = courseRepository.save(course);
        return courseMapper.courseToCourseDto(savedCourse);

    }

    public String deleteCourse(Long id) {
        try {
            courseRepository.deleteById(id);
            return "Course deleted";
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public CourseDto getCourseByUrl(String url) {
        Optional<Course> oCourse = courseRepository.findByUrl(url);
        if (oCourse.isEmpty()) {
            throw new AppException("Course not found", HttpStatus.NOT_FOUND);
        }
        return courseMapper.courseToCourseDto(oCourse.get());
    }

    public boolean isUserIsAuthor(String username, String courseUrl) {
        Optional<Course> oCourse = courseRepository.findByUrl(courseUrl);
        if (oCourse.isEmpty()) {
            throw new AppException("Course not found", HttpStatus.NOT_FOUND);
        }
        return oCourse.get().getAuthorUsername().equals(username);
    }

    public Response updateCourse(String courseUrl, CourseDto courseDto) {
        Optional<Course> oCourse = courseRepository.findByUrl(courseUrl);
        if (oCourse.isEmpty()) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("Course not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        Course course = oCourse.get();
        course.setDescription(courseDto.getDescription());
        course.setShortDescription(courseDto.getShortDescription());
        course.setCategory(courseDto.getCategory());
        course.setLanguage(courseDto.getLanguage());
        course.setImageUrl(courseDto.getImageUrl());
        course.setLastUpdateDate(new Date());
        course.setSections(courseDto.getSections());
        courseRepository.save(course);
        return new Response().builder()
                .isSuccess(true)
                .message("Course updated")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Response isUserHaveAccessToCourse(String username, String url) {
        Optional<Course> oCourse = courseRepository.findByUrl(url);
        Optional<User> oUser = userService.findByUsername(username);
        if (!oCourse.isPresent() || !oUser.isPresent()) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("Course or user not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        if (oCourse.get().isPublic()) {
            return new Response().builder()
                    .isSuccess(true)
                    .message("Course is public")
                    .httpStatus(null)
                    .build();
        }

        if (isUserIsAuthor(username, url)) {
            return new Response().builder()
                    .isSuccess(true)
                    .message("")
                    .httpStatus(null)
                    .build();
        } else if (oUser.get().getCoursesId().contains(oCourse.get().getId())) {
            return new Response().builder()
                    .isSuccess(true)
                    .message("")
                    .httpStatus(null)
                    .build();
        }

        return new Response().builder()
                .isSuccess(false)
                .message("You have not access to this course")
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();

    }

    public Response joinCourse(String username, String courseUrl) {
        Optional<Course> oCourse = courseRepository.findByUrl(courseUrl);
        Optional<User> oUser = userService.findByUsername(username);

        if (oCourse.isEmpty()) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("Course not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        if (oUser.isEmpty()) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("User not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        if (oUser.get().getCoursesId().contains(oCourse.get().getId())) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("You already joined this course")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }

        oUser.get().getCoursesId().add(oCourse.get().getId());
        //Update course user count
        oCourse.get().setStudentsCount(oCourse.get().getStudentsCount() + 1);

        courseRepository.save(oCourse.get());
        userService.save(oUser.get());
        return new Response().builder()
                .isSuccess(true)
                .message("You joined this course")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Response logoutCourse(String username, String courseUrl) {
        Optional<Course> oCourse = courseRepository.findByUrl(courseUrl);
        Optional<User> oUser = userService.findByUsername(username);

        if (oCourse.isEmpty()) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("Course not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        if (oUser.isEmpty()) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("User not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        if (!oUser.get().getCoursesId().contains(oCourse.get().getId())) {
            return new Response().builder()
                    .isSuccess(false)
                    .message("You not joined this course")
                    .httpStatus(HttpStatus.CONFLICT)
                    .build();
        }

        oUser.get().getCoursesId().remove(oCourse.get().getId());
        //Update course user count
        oCourse.get().setStudentsCount(oCourse.get().getStudentsCount() - 1);

        courseRepository.save(oCourse.get());
        userService.save(oUser.get());
        return new Response().builder()
                .isSuccess(true)
                .message("You logout from this course")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public List<Course> getUserCourses(User user) {
        return courseRepository.findAllById(user.getCoursesId());
    }

    public List<CourseDto> searchCourses(String name, String category) {

        if (name != null && category == null) {
            return courseMapper.coursesToCoursesDto(courseRepository.findAllByNameContainingAndIsPublic(name, true));
        }

        if (name == null && category != null) {
            return courseMapper.coursesToCoursesDto(courseRepository.findAllByCategoryContainingAndIsPublic(category, true));
        }

        if (name != null && category != null) {
            return courseMapper.coursesToCoursesDto(courseRepository.findAllByNameContainingAndCategoryContainingAndIsPublic(name, category, true));
        }


//        return courseMapper.coursesToCoursesDto(courseRepository.findAllByIsPublic(true));
        return null;
    }
}
