package org.tasker.usermanagementservice.mappers;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.tasker.common.api.dto.user.GetUserResponse;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    GetUserResponse toGetUserResponse(UserRepresentation user);
}
