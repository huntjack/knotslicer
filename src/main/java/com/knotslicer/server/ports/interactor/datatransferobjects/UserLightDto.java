package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.ZoneId;

@JsonDeserialize(as = UserLightDtoImpl.class)
public interface UserLightDto extends Linkable {
    Long getUserId();
    void setUserId(Long userId);
    String getUserName();
    void setUserName(String userName);
    String getUserDescription();
    void setUserDescription(String userDescription);
    ZoneId getTimeZone();
    void setTimeZone(ZoneId timeZone);
}
