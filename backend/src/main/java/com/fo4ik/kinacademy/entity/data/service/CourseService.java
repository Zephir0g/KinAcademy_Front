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

      if(oCourse.isPresent()){
         throw new AppException("Course already exists", HttpStatus.BAD_REQUEST);
      }

      Course course = courseMapper.singUpCourseDtoToCourse(singUpCourseDto);
      course.setAuthorId(userId);
      course.setLastUpdateDate(new Date());
      course.setStatus(Status.INACTIVE);

      Course savedCourse = courseRepository.save(course);
      return courseMapper.courseToCourseDto(savedCourse);

   }

}
