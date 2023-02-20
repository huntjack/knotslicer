package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = LinkImpl.class)
public interface Link {
    String getLink();
    void setLink(String link);
    String getRel();
    void setRel(String rel);
}
