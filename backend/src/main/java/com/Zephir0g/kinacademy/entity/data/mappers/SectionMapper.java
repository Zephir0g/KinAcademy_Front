package com.Zephir0g.kinacademy.entity.data.mappers;

import com.Zephir0g.kinacademy.dto.course.SectionsDto;
import com.Zephir0g.kinacademy.entity.course.Section;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SectionMapper {

    Section toSection(SectionsDto sectionsDto);

    List<Section> sectionsDtoToSections(List<SectionsDto> sectionsDtos);

}
