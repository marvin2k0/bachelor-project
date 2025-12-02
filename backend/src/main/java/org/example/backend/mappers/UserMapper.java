package org.example.backend.mappers;

import org.example.backend.user.User;
import org.example.backend.user.dto.UserCreationDto;
import org.example.backend.user.dto.UserDto;
import org.example.backend.user.dto.UserUpdateDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    UserDto toDto(User user);

    List<UserDto> toDto(List<User> users);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreationDto user);

    @Mapping(target = "id", ignore = true)
    void updateEntity(UserUpdateDto dto, @MappingTarget User user);
}
