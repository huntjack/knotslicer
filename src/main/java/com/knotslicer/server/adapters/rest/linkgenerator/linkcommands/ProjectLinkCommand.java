package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class ProjectLinkCommand extends LinkCommand {

    protected ProjectDto projectDto;
    protected UriInfo uriInfo;
    public ProjectLinkCommand(LinkReceiver linkReceiver, ProjectDto projectDto, UriInfo uriInfo) {
        super(linkReceiver);
        this.projectDto = projectDto;
        this.uriInfo = uriInfo;
    }
    @Override
    public void execute() {
        URI projectUri = linkReceiver.getUriForProject(uriInfo.getBaseUriBuilder(), projectDto.getProjectId(), projectDto.getUserId());
        this.selfLink = projectUri;
        projectDto.addLink(projectUri.toString(), "self");
        URI userUri = linkReceiver.getUriForUser(uriInfo.getBaseUriBuilder(), projectDto.getUserId());
        projectDto.addLink(userUri.toString(), "user");
    }
}
