package com.Zephir0g.kinacademy.entity.data.mappers;


import com.Zephir0g.kinacademy.dto.course.VideoDto;
import com.Zephir0g.kinacademy.entity.course.Video;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VideoMapper {

    VideoDto toVideoDto(Video video);

    Video toVideo(VideoDto videoDto);

    List<VideoDto> toVideoDtoList(List<Video> videos);

    List<Video> toVideoList(List<VideoDto> videoDtos);

}
