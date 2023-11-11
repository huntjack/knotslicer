package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserDtoImpl.class)
public interface UserDto extends UserLightDto {
    String getEmail();
    void setEmail(String email);

}
