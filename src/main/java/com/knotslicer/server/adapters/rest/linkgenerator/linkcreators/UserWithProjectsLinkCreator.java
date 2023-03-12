package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.WithChildren;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.UserWithProjectsLinkCommand;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.USER)
@WithChildren(ProcessType.PROJECT)
@ApplicationScoped
public class UserWithProjectsLinkCreator implements LinkCreator<UserLightDto> {
    @Override
    public LinkCommand<UserLightDto> createLinkCommand(LinkReceiver linkReceiver, UserLightDto userLightDto, UriInfo uriInfo) {
        return new UserWithProjectsLinkCommand(linkReceiver, userLightDto, uriInfo);
    }
}
