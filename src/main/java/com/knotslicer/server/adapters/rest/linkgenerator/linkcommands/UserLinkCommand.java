package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class UserLinkCommand extends LinkCommand {
    private UserLightDto userLightDto;
    private UriInfo uriInfo;
    public UserLinkCommand(LinkReceiver linkReceiver, UserLightDto userLightDto, UriInfo uriInfo) {
        super(linkReceiver);
        this.userLightDto = userLightDto;
        this.uriInfo = uriInfo;
    }
    @Override
    public void execute() {
        URI uri = linkReceiver.getUriForUser(uriInfo.getBaseUriBuilder(), userLightDto.getUserId());
        this.selfLink = uri;
        userLightDto.addLink(uri.toString(), "self");
    }
}
