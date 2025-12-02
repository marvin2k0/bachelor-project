package org.example.backend.mappers;

import org.example.backend.survey.Survey;
import org.example.backend.survey.dto.SurveyCreationDto;
import org.example.backend.survey.dto.SurveyDto;
import org.example.backend.survey.dto.SurveyUpdateDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class}
)
public interface SurveyMapper {
    SurveyDto toDto(Survey survey);

    List<SurveyDto> toDto(List<Survey> surveys);

    @Mapping(target = "id", ignore = true)
    Survey toEntity(SurveyCreationDto survey);

    @Mapping(target = "id", ignore = true)
    void updateEntity(Long id, SurveyUpdateDto entity, @MappingTarget Survey survey);
}
