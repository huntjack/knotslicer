package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.UserLinkCommand;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.USER)
@Default
@ApplicationScoped
public class UserLinkCreator implements LinkCreator<UserLightDto> {
    @Override
    public LinkCommand<UserLightDto> createLinkCommand(LinkReceiver linkReceiver, UserLightDto userLightDto, UriInfo uriInfo) {
        return new UserLinkCommand(linkReceiver, userLightDto, uriInfo);
    }
}
