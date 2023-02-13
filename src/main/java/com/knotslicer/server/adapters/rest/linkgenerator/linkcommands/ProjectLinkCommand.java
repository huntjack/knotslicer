package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class ProjectLinkCommand extends LinkCommand<ProjectDto> {
    public ProjectLinkCommand(LinkReceiver linkReceiver, ProjectDto projectDto, UriInfo uriInfo) {
        super(linkReceiver, projectDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI projectUri = linkReceiver.getUriForProject(uriInfo.getBaseUriBuilder(), dto.getProjectId(), dto.getUserId());
        dto.addLink(projectUri.toString(), "self");
        URI userUri = linkReceiver.getUriForUser(uriInfo.getBaseUriBuilder(), dto.getUserId());
        dto.addLink(userUri.toString(), "user");
        return projectUri;
    }
}
