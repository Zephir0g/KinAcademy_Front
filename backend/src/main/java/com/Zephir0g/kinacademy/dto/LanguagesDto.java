package com.Zephir0g.kinacademy.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record LanguagesDto(
        @Schema(description = "Language code", example = "english", name = "code")
        String code,
        @Schema(description = "Language name", example = "English", name = "label")
        String label) {
}