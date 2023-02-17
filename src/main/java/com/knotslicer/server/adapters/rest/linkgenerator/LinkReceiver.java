package com.knotslicer.server.adapters.rest.linkgenerator;

import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.Map;

public interface LinkReceiver {
    URI getUriForUser(UriBuilder uriBuilder, Long userId);
    URI getUriForProject(UriBuilder uriBuilder, Map<String, Long> ids);
    URI getUriForMember(UriBuilder uriBuilder, Map<String, Long> ids);
    URI getUriForSchedule(UriBuilder baseUriBuilder, Map<String, Long> ids);
}
