package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

public class UserLinkCommand extends LinkCommand<UserLightDto> {
    public UserLinkCommand(LinkReceiver linkReceiver, UserLightDto userLightDto, UriInfo uriInfo) {
        super(linkReceiver, userLightDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI userUri = linkReceiver.getUriForUser(
                uriInfo.getBaseUriBuilder(),
                dto.getUserId());
        dto.addLink(
                userUri.toString(),
                "self");
        return userUri;
    }
}
