package com.knotslicer.server.adapters.rest.linkcommands;

import com.knotslicer.server.adapters.rest.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class UserLightLinkCommand extends LinkCommand {
    private LinkParamWrapper<UserLightDto> linkParamWrapper;
    public UserLightLinkCommand(LinkReceiver linkReceiver, LinkParamWrapper<UserLightDto> linkParamWrapper) {
        super(linkReceiver);
        this.linkParamWrapper = linkParamWrapper;
    }
    @Override
    public void execute() {
        UserLightDto userLightDto = linkParamWrapper.getDto();
        URI uri = linkReceiver.getUriForUser(linkParamWrapper.getUriInfo(), userLightDto);
        userLightDto.addLink(uri.toString(), linkParamWrapper.getRel());
    }
}
