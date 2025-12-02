package org.example.backend.mappers;

import org.example.backend.group.Group;
import org.example.backend.group.dto.GroupCreationDto;
import org.example.backend.group.dto.GroupDto;
import org.example.backend.group.dto.GroupUpdateDto;
import org.example.backend.survey.Survey;
import org.example.backend.survey.SurveyRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class GroupMapper {
    @Autowired
    protected SurveyRepository surveyRepository;

    public abstract GroupDto toDto(Group group);

    public abstract List<GroupDto> toDto(List<Group> groups);

    public abstract void updateEntity(GroupUpdateDto dto, @MappingTarget Group group);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "survey", source = "surveyId", qualifiedByName = "surveyIdToSurvey")
    public abstract Group toEntity(GroupCreationDto dto);

    @Named("surveyIdToSurvey")
    protected Survey surveyIdToSurvey(Long surveyId) {
        if (surveyId == null) {
            return null;
        }
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found with id: " + surveyId));
    }
}
