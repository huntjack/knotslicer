package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.ProjectLinkCommand;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.PROJECT)
@Default
@ApplicationScoped
public class ProjectLinkCreator implements LinkCreator<ProjectDto> {
    @Override
    public LinkCommand<ProjectDto> createLinkCommand(LinkReceiver linkReceiver, ProjectDto projectDto, UriInfo uriInfo) {
        return new ProjectLinkCommand(linkReceiver, projectDto, uriInfo);
    }
}
