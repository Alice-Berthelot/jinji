package com.jinji.backend.mapper;

import com.jinji.backend.model.dto.response.NotificationDTO;
import com.jinji.backend.model.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    NotificationDTO toDto(Notification notification);
}