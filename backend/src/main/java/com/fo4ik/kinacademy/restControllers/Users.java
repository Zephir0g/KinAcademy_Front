package com.fo4ik.kinacademy.restControllers;

import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.dto.course.CourseDto;
import com.fo4ik.kinacademy.dto.user.UserDto;
import com.fo4ik.kinacademy.entity.course.Course;
import com.fo4ik.kinacademy.entity.data.service.CourseService;
import com.fo4ik.kinacademy.entity.data.service.UserService;
import com.fo4ik.kinacademy.entity.user.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Methods for getting users")
public class Users {

    private final UserService userService;
    private final CourseService courseService;


    //TODO rewrite this method or create new method for update user
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ResponseEntity<?> updateUser(
            @Parameter(description = "Username", required = true)
            @RequestParam("username") String username,
            @RequestHeader("Authorization") String token
    ) {
        token = token.replace("Bearer ", "");

        System.out.println("token: " + token);

        Response isUserActive = userService.isUserActive(username);
        if (!isUserActive.isSuccess()) {
            return ResponseEntity.status(isUserActive.getHttpStatus()).body(isUserActive);
        }

        UserDto userDto = userService.getUserByUsernameDto(username);
        userDto.setSECURE_TOKEN(token);
        return ResponseEntity.ok(userDto);
    }

    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    public ResponseEntity<?> getUserCourses(
            @Parameter(description = "Username", required = true)
            @RequestParam("username") String username
    ) {

        Response isUserActive = userService.isUserActive(username);
        if (!isUserActive.isSuccess()) {
            return ResponseEntity.status(isUserActive.getHttpStatus()).body(isUserActive);
        }

        User user = userService.getUserByUsername(username);
        List<CourseDto> userCourses = courseService.getUserCourses(user);

        return ResponseEntity.ok(userCourses);
    }


}
