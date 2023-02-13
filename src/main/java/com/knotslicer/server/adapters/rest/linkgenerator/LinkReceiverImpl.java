package com.knotslicer.server.adapters.rest.linkgenerator;

import com.knotslicer.server.adapters.rest.UserResourceImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

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
    public URI getUriForProject(UriBuilder uriBuilder, Long projectId, Long userId) {
        String baseUri = uriBuilder
                .path(UserResourceImpl.class)
                .build()
                .toString();
        String secondHalfOfUri = "/{userId}/projects/{projectId}";
        String template = baseUri + secondHalfOfUri;
        Map<String, Long> parameters = new HashMap<>(3);
        parameters.put(
                "userId",
                userId);
        parameters.put(
                "projectId",
                projectId);
        UriBuilder finalUriBuilder = UriBuilder.fromPath(template);
        return finalUriBuilder
                .buildFromMap(parameters);
    }
    @Override
    public URI getUriForMembers(UriBuilder baseUriBuilder, Long memberId, Long userId) {
        String baseUri = baseUriBuilder
                .path(UserResourceImpl.class)
                .build()
                .toString();
        String secondHalfOfUri = "/{userId}/members/{memberId}";
        String template = baseUri + secondHalfOfUri;
        Map<String, Long> parameters = new HashMap<>(3);
        parameters.put(
                "userId",
                userId);
        parameters.put(
                "memberId",
                memberId);
        UriBuilder uriBuilder = UriBuilder.fromPath(template);
        return uriBuilder
                .buildFromMap(parameters);
    }
}
