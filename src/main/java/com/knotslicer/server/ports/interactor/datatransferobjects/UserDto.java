package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.ZoneId;
import java.util.List;

@JsonDeserialize(as = UserDtoImpl.class)
public interface UserDto extends UserLightDto {
    String getEmail();
    void setEmail(String email);

}
