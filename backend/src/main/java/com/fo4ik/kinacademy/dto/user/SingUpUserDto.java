package com.fo4ik.kinacademy.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data for user credential", name = "SingUpUserDto")
public record SingUpUserDto(
        @Schema(description = "User first name", example = "John")
        @NotBlank
        String firstName,
        @Schema(description = "User surname", example = "Doe")
        @NotBlank
        String surname,
        @Schema(description = "User login", example = "john123")
        @NotBlank
        String login,
        @Schema(description = "User password", example = "john123")
        @NotBlank
        char[] password,

        @Schema(description = "User language", example = "English")
        String language,
        @Schema(description = "User email", example = "john.example@gmail.com")
        @NotBlank
        String email) {
}
