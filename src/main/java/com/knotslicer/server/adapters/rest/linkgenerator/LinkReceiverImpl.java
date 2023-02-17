package com.knotslicer.server.adapters.rest.linkgenerator;

import com.knotslicer.server.adapters.rest.UserResourceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class LinkReceiverImpl implements LinkReceiver {
    @Override
    public URI getUriForUser(UriBuilder uriBuilder, Long userId) {
        return uriBuilder
                .path(UserResourceImpl.class)
                .path(Long.toString(userId))
                .build();
    }
    @Override
    public URI getUriForProject(UriBuilder uriBuilder, Map<String,Long> ids) {
        String baseUri = uriBuilder
                .path(UserResourceImpl.class)
                .build()
                .toString();
        String secondHalfOfUri = "/{projectOwnerId}/projects/{projectId}";
        String template = baseUri + secondHalfOfUri;
        UriBuilder finalUriBuilder = UriBuilder.fromPath(template);
        return finalUriBuilder
                .buildFromMap(ids);
    }
    @Override
    public URI getUriForMember(UriBuilder baseUriBuilder, Map<String,Long> ids) {
        String baseUri = baseUriBuilder
                .path(UserResourceImpl.class)
                .build()
                .toString();
        String secondHalfOfUri = "/{userId}/members/{memberId}";
        String template = baseUri + secondHalfOfUri;
        UriBuilder uriBuilder = UriBuilder.fromPath(template);
        return uriBuilder
                .buildFromMap(ids);
    }
    public URI getUriForSchedule(UriBuilder baseUriBuilder, Map<String,Long> ids) {
        String baseUri = baseUriBuilder
                .path(UserResourceImpl.class)
                .build()
                .toString();
        String secondHalfOfUri = "/{userId}/members/{memberId}/schedules/{scheduleId}";
        String template = baseUri + secondHalfOfUri;
        UriBuilder uriBuilder = UriBuilder.fromPath(template);
        return uriBuilder
                .buildFromMap(ids);
    }
}
