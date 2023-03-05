package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class UserWithProjectsLinkCommand extends UserLinkCommand {
    public UserWithProjectsLinkCommand(LinkReceiver linkReceiver, UserLightDto userLightDto, UriInfo uriInfo) {
        super(linkReceiver, userLightDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI userUri = super.execute();
        List<ProjectDto> projectDtos = dto.getProjects();
        for(ProjectDto projectDto :projectDtos) {
            URI projectUri = linkReceiver
                    .getUriForProject(
                            uriInfo.getBaseUriBuilder(),
                            projectDto.getProjectId());
            projectDto.addLink(
                    projectUri.toString(),
                    "project");
        }
        return userUri;
    }
}
