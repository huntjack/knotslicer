package com.knotslicer.server.adapters.rest.linkgenerator;

import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;

public interface LinkReceiver {
    URI getUriForUser(UriBuilder uriBuilder, Long userId);
    URI getUriForProject(UriBuilder uriBuilder, Long projectId, Long userId);
    URI getUriForMember(UriBuilder uriBuilder, Long memberId, Long userId);
}
