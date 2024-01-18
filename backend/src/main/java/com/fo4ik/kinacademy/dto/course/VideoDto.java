package com.fo4ik.kinacademy.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data for video entity", name = "VideoDto")
public class VideoDto {

    @Schema(description = "Video id", example = "1")
    Long id;

    @Schema(description = "Video name", example = "Java")
    String name;

    @Schema(description = "Video url", example = "https://example.com/video.mp4")
    String urlToVideo;

    @Schema(description = "Video is watched by this user", example = "false")
    Boolean isWatched;
}
