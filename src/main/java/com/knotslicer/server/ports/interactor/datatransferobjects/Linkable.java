package com.knotslicer.server.ports.interactor.datatransferobjects;

import java.util.List;

public interface Linkable {
    void addLink(String url, String rel);
    List<Link> getLinks();
    void setLinks(List<Link> links);
}
