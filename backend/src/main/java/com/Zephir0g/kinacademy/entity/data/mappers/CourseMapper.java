package com.Zephir0g.kinacademy.entity.data.mappers;

import com.Zephir0g.kinacademy.dto.course.CourseDto;
import com.Zephir0g.kinacademy.dto.course.SingUpCourseDto;
import com.Zephir0g.kinacademy.entity.course.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {
    Course singUpCourseDtoToCourse(SingUpCourseDto singUpCourseDto);

    CourseDto courseToCourseDto(Course course);

    List<CourseDto> coursesToCoursesDto(List<Course> courses);
}
