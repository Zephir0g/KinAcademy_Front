package com.fo4ik.kinacademy.restControllers;

import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.dto.user.CredentialDto;
import com.fo4ik.kinacademy.dto.user.UserDto;
import com.fo4ik.kinacademy.entity.data.service.UserService;
import com.fo4ik.kinacademy.entity.user.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Methods for getting users")
public class Users {

    private final UserService userService;


    //TODO rewrite this method or create new method for update user
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ResponseEntity<?> updateUser(
            @Parameter(description = "Username", required = true)
            @RequestParam("username") String username,
            @RequestHeader("Authorization") String token
    ) {
        token = token.replace("Bearer ", "");

        Response isUserActive = userService.isUserActive(username);
        if (!isUserActive.isSuccess()){
            return ResponseEntity.status(isUserActive.getHttpStatus()).body(isUserActive);
        }

        UserDto userDto = userService.getUserByUsernameDto(username);
        userDto.setSECURE_TOKEN(token);
        return ResponseEntity.ok(userDto);

    }


}
