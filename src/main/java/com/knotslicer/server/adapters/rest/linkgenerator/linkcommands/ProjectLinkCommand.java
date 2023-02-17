package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ProjectLinkCommand extends LinkCommand<ProjectDto> {
    public ProjectLinkCommand(LinkReceiver linkReceiver, ProjectDto projectDto, UriInfo uriInfo) {
        super(linkReceiver, projectDto, uriInfo);
    }
    @Override
    public URI execute() {
        Map<String,Long> ids = new HashMap<>(3);
        ids.put("projectOwnerId",
                dto.getUserId());
        ids.put("projectId",
                dto.getProjectId());
        URI projectUri = linkReceiver
                .getUriForProject(
                        uriInfo.getBaseUriBuilder(),
                        ids);
        dto.addLink(projectUri
                        .toString(),
                "self");
        URI userUri = linkReceiver
                .getUriForUser(
                        uriInfo.getBaseUriBuilder(),
                        dto.getUserId());
        dto.addLink(
                userUri.toString(),
                "user");
        return projectUri;
    }
}
