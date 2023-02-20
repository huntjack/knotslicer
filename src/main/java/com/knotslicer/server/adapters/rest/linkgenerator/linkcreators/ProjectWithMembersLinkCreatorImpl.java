package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.ProjectWithMembersLinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@ProjectWithMembersLinkCreator
@ApplicationScoped
public class ProjectWithMembersLinkCreatorImpl implements LinkCreator<ProjectDto> {
    @Override
    public LinkCommand<ProjectDto> createLinkCommand(LinkReceiver linkReceiver, ProjectDto projectDto, UriInfo uriInfo) {
        return new ProjectWithMembersLinkCommand(linkReceiver, projectDto, uriInfo);
    }
}
