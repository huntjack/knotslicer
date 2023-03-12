package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.WithChildren;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.UserWithEventsLinkCommand;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.USER)
@WithChildren(ProcessType.EVENT)
@ApplicationScoped
public class UserWithEventsLinkCreator implements LinkCreator <UserLightDto> {
    @Override
    public LinkCommand<UserLightDto> createLinkCommand(LinkReceiver linkReceiver, UserLightDto userLightDto, UriInfo uriInfo) {
        return new UserWithEventsLinkCommand(linkReceiver, userLightDto, uriInfo);
    }
}
