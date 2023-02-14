package com.knotslicer.server.adapters.rest.linkgenerator;

import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.Map;

public interface LinkReceiver {
    URI getUriForUser(UriBuilder uriBuilder, Long userId);
    URI getUriForProject(UriBuilder uriBuilder, Map<String, Long> primaryKeys);
    URI getUriForMember(UriBuilder uriBuilder, Map<String, Long> primaryKeys);
    URI getUriForSchedule(UriBuilder baseUriBuilder, Map<String, Long> primaryKeys);
}
