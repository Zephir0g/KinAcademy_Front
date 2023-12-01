package com.fo4ik.kinacademy.dto.course;

import com.fo4ik.kinacademy.entity.course.Video;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data for section entity", name = "SectionDto")
public class SectionsDto {
    @Schema(description = "Section id", example = "1")
    Long id;

    @Schema(description = "Section name", example = "Section 1")
    @NotBlank
    String name;

    @Schema(description = "Section videos", example = "See VideoDto")
    List<Video> videos;

}
