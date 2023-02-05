package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.ZoneId;

@JsonDeserialize(as = UserLightDtoImpl.class)
public interface UserLightDto extends Linkable {
    Long getUserId();
    void setUserId(Long userId);
    public String getUserName();
    public void setUserName(String userName);
    public String getUserDescription();
    public void setUserDescription(String userDescription);
    ZoneId getTimeZone();
    void setTimeZone(ZoneId timeZone);
}
