package com.fo4ik.kinacademy.restControllers;

import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.dto.user.UserDto;
import com.fo4ik.kinacademy.entity.data.service.UserService;
import com.fo4ik.kinacademy.exceptions.AppException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Methods for getting users")
@SecurityRequirement(name = "Bearer token")
public class Users {

    private final UserService userService;

    @Operation(summary = "Get user by id", description = "Get user by id, needed SECURE_TOKEN", tags = {"Users"})
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "User id", required = true)
            @PathVariable Long id,
            @Parameter(hidden = true)
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String SECURE_TOKEN
    ) {
        Response response = userService.isUserValid(SECURE_TOKEN, id);
        if(!response.isSuccess()){
            throw new AppException(response.getMessage(), response.getHttpStatus());
        }
        return ResponseEntity.ok(userService.getUserById(id));
    }

}
