package com.knotslicer.server.adapters.rest.linkgenerator;

import com.knotslicer.server.adapters.rest.MemberResourceImpl;
import com.knotslicer.server.adapters.rest.ProjectResourceImpl;
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
    public URI getUriForProject(UriBuilder uriBuilder, Long projectId) {
        return uriBuilder
                .path(ProjectResourceImpl.class)
                .path(Long.toString(projectId))
                .build();
    }
    @Override
    public URI getUriForMember(UriBuilder uriBuilder, Long memberId) {
        return uriBuilder
                .path(MemberResourceImpl.class)
                .path(Long.toString(memberId))
                .build();
    }
    public URI getUriForSchedule(UriBuilder baseUriBuilder, Long scheduleId, Long memberId) {
        String baseUri = baseUriBuilder
                .path(MemberResourceImpl.class)
                .build()
                .toString();
        String secondHalfOfUri = "/{memberId}/schedules/{scheduleId}";
        String template = baseUri + secondHalfOfUri;
        UriBuilder uriBuilder = UriBuilder.fromPath(template);
        Map<String,Long> ids = new HashMap<>(3);
        ids.put("memberId",
                memberId);
        ids.put("scheduleId",
                scheduleId);
        return uriBuilder
                .buildFromMap(ids);
    }
}
