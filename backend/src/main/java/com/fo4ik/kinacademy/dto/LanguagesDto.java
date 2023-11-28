package com.fo4ik.kinacademy.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record LanguagesDto(
        @Schema(description = "Language code", example = "english")
        String code,
        @Schema(description = "Language name", example = "English")
        String label) {
}
