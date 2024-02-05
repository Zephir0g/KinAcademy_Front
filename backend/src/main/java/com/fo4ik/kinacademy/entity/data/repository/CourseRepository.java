package com.fo4ik.kinacademy.entity.data.repository;

import com.fo4ik.kinacademy.entity.course.Course;
import com.fo4ik.kinacademy.entity.course.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT DISTINCT v FROM Course c JOIN c.sections s JOIN s.videos v JOIN v.usersWhoWatched u WHERE u.username = :username")
    List<Video> findVideosWatchedByUser(@Param("username") String username);

    @Query("SELECT c FROM Course c ORDER BY c.studentsCount DESC")
    List<Course> findTop5CoursesByStudentsCount();
}
