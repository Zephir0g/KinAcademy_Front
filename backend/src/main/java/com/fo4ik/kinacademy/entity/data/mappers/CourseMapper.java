package com.fo4ik.kinacademy.entity.data.mappers;

import com.fo4ik.kinacademy.dto.course.CourseDto;
import com.fo4ik.kinacademy.dto.course.SingUpCourseDto;
import com.fo4ik.kinacademy.entity.course.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {
    Course singUpCourseDtoToCourse(SingUpCourseDto singUpCourseDto);

    CourseDto courseToCourseDto(Course course);
}
