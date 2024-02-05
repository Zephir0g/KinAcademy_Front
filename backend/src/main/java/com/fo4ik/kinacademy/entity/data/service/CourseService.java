package com.fo4ik.kinacademy.entity.data.service;

import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.dto.CategoryDTO;
import com.fo4ik.kinacademy.dto.course.CourseDto;
import com.fo4ik.kinacademy.dto.course.SectionsDto;
import com.fo4ik.kinacademy.dto.course.SingUpCourseDto;
import com.fo4ik.kinacademy.dto.course.VideoDto;
import com.fo4ik.kinacademy.entity.course.Course;
import com.fo4ik.kinacademy.entity.course.Section;
import com.fo4ik.kinacademy.entity.course.Video;
import com.fo4ik.kinacademy.entity.data.mappers.CourseMapper;
import com.fo4ik.kinacademy.entity.data.mappers.SectionMapper;
import com.fo4ik.kinacademy.entity.data.mappers.VideoMapper;
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
    private final SectionMapper sectionMapper;
    private final VideoMapper videoMapper;
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
        User user = userService.getUserByUsername(username);
        user.getCoursesId().add(course.getId());
        userService.save(user);

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

    public CourseDto getCourseByUrl(String url, String username) {
        Optional<Course> oCourse = courseRepository.findByUrl(url);
        if (oCourse.isEmpty()) {
            throw new AppException("Course not found", HttpStatus.NOT_FOUND);
        }

        Course course = oCourse.get();
        CourseDto courseDto = courseMapper.courseToCourseDto(oCourse.get());
        Optional<User> oUser = userService.findByUsername(username);


        /// Get the list of watched videos for the user
        List<Video> watchedVideos = courseRepository.findVideosWatchedByUser(username);

        // Iterate through course sections and videos in courseDto and mark them as watched if they exist in watchedVideos
        for (SectionsDto section : courseDto.getSections()) {
            for (VideoDto videoDto : section.getVideos()) {
                videoDto.setIsWatched(isVideoWatched(watchedVideos, videoDto.getId()));
            }
        }

        return courseDto;
    }

    private boolean isVideoWatched(List<Video> watchedVideos, Long videoId) {
        return watchedVideos.stream().anyMatch(video -> video.getId().equals(videoId));
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
        course.setSections(sectionMapper.sectionsDtoToSections(courseDto.getSections()));
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

    public List<CourseDto> getUserCourses(User user) {
        List<Course> courses = courseRepository.findAllById(user.getCoursesId());
        List<CourseDto> courseDtos = courseMapper.coursesToCoursesDto(courses);
        return courseDtos;
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

    public Response changeStatusWatchedVideo(String username, String courseUrl, String videoUrl) {

        // Find the user by username
        Optional<User> oUser = userService.findByUsername(username);
        if (oUser.isEmpty()) {
            return Response.builder()
                    .isSuccess(false)
                    .message("User not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        // Find the course by courseUrl
        Optional<Course> oCourse = courseRepository.findByUrl(courseUrl);
        if (oCourse.isEmpty()) {
            return Response.builder()
                    .isSuccess(false)
                    .message("Course not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        // Find the video within the course by videoUrl
        Course course = oCourse.get();
        Video videoToUpdate = null;
        for (Section section : course.getSections()) {
            for (Video video : section.getVideos()) {
                if (video.getUrlToVideo().equals(videoUrl)) {
                    videoToUpdate = video;
                    break;
                }
            }
        }

        if (videoToUpdate == null) {
            return Response.builder()
                    .isSuccess(false)
                    .message("Video not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        // Update the user's watched videos list
        User user = oUser.get();
        if (user.getWatchedVideos().contains(videoToUpdate)) {
            user.getWatchedVideos().remove(videoToUpdate);
            userService.save(user); // Save the updated user

            return Response.builder()
                    .isSuccess(true)
                    .message("Video marked as not watched")
                    .httpStatus(HttpStatus.OK)
                    .build();
        } else {
            user.getWatchedVideos().add(videoToUpdate);
            userService.save(user); // Save the updated user

            return Response.builder()
                    .isSuccess(true)
                    .message("Video marked as watched")
                    .httpStatus(HttpStatus.OK)
                    .build();
        }
    }

    public List<CourseDto> getPopularCourses() {
        return courseMapper.coursesToCoursesDto(courseRepository.findTop5CoursesByStudentsCount());
    }
}
