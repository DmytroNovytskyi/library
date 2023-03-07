package com.my.library.mapper;

import com.my.library.dto.UserDto;
import com.my.library.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserDto mapUserDto(User user);

    User mapUser(UserDto userDto);

    void mapPresentFields(@MappingTarget User toUser, User fromUser);
}
