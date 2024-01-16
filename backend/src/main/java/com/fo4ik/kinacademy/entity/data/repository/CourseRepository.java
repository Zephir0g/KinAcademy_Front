package com.fo4ik.kinacademy.entity.data.repository;

import com.fo4ik.kinacademy.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);

    void deleteById(Long id);


    List<Course> findAllByCategoryContainingAndIsPublic(String category, boolean isPublic);
    List<Course> findAllByNameContainingAndIsPublic(String name, boolean isPublic);

    List<Course> findAllByNameContainingAndCategoryContainingAndIsPublic(String name, String category, boolean isPublic);

    Optional<Course> findByUrl(String url);
}
