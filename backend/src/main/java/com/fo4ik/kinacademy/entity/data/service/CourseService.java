package com.fo4ik.kinacademy.entity.data.service;

import com.fo4ik.kinacademy.dto.course.CourseDto;
import com.fo4ik.kinacademy.dto.course.SingUpCourseDto;
import com.fo4ik.kinacademy.entity.course.Course;
import com.fo4ik.kinacademy.entity.data.mappers.CourseMapper;
import com.fo4ik.kinacademy.entity.data.repository.CourseRepository;
import com.fo4ik.kinacademy.entity.user.Status;
import com.fo4ik.kinacademy.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;


    public CourseDto createCourse(SingUpCourseDto singUpCourseDto, Long userId) {
        Optional<Course> oCourse = courseRepository.findByName(singUpCourseDto.name());

        if (oCourse.isPresent()) {
            throw new AppException("Course already exists", HttpStatus.BAD_REQUEST);
        }

        Course course = courseMapper.singUpCourseDtoToCourse(singUpCourseDto);
        course.setAuthorId(userId);
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

    public boolean isUserIsAuthor(Long userId, String courseUrl) {
        Optional<Course> oCourse = courseRepository.findByUrl(courseUrl);
        if (oCourse.isEmpty()) {
            throw new AppException("Course not found", HttpStatus.NOT_FOUND);
        }
        return oCourse.get().getAuthorId().equals(userId);
    }
}
