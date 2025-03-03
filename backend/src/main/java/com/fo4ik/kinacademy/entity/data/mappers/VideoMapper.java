package com.fo4ik.kinacademy.entity.data.mappers;


import com.fo4ik.kinacademy.dto.course.VideoDto;
import com.fo4ik.kinacademy.entity.course.Video;
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
