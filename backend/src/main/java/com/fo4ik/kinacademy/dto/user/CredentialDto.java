package com.fo4ik.kinacademy.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CredentialDto(
        @Schema(description = "User login", example = "john123", name = "login")
                @NotBlank
        String login,
        @Schema(description = "User password", example = "john123", name = "password")
        @NotBlank
        char[] password) {
}
