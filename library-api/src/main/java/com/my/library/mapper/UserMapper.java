package com.my.library.mapper;

import com.my.library.dto.UserDto;
import com.my.library.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * A mapper interface for converting between User and UserDto objects.
 * Also allows to map present fields for User objects.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * An instance of the mapper.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Converts a User object to a UserDto object ignoring password.
     *
     * @param user The User object to convert
     * @return The corresponding UserDto object
     */
    @Mapping(target = "password", ignore = true)
    UserDto mapUserDto(User user);

    /**
     * Converts a UserDto object to a User object.
     *
     * @param userDto The UserDto object to convert
     * @return The corresponding User object
     */
    User mapUser(UserDto userDto);

    /**
     * Copies the present fields of a User object from another User object.
     *
     * @param toUser   The User object to copy the fields to
     * @param fromUser The User object to copy the fields from
     */
    void mapPresentFields(@MappingTarget User toUser, User fromUser);
}
