package com.knotslicer.server.adapters.rest.linkgenerator;

import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.Map;

public interface LinkReceiver {
    URI getUriForUser(UriBuilder uriBuilder, Long userId);
    URI getUriForProject(UriBuilder uriBuilder, Long projectId);
    URI getUriForMember(UriBuilder uriBuilder, Long memberId);
    URI getUriForSchedule(UriBuilder baseUriBuilder, Long scheduleId, Long memberId);
}
