package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.ProjectLinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@ProjectLinkCreator
@ApplicationScoped
public class ProjectLinkCreatorImpl implements LinkCreator<ProjectDto> {
    @Override
    public LinkCommand createLinkCommand(LinkReceiver linkReceiver, ProjectDto projectDto, UriInfo uriInfo) {
        return new ProjectLinkCommand(linkReceiver, projectDto, uriInfo);
    }
}
