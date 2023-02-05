package com.knotslicer.server.adapters.rest.linkcommands;

import com.knotslicer.server.adapters.rest.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class UserLinkCommand extends LinkCommand {
    private LinkParamWrapper<UserDto> linkParamWrapper;
    public UserLinkCommand(LinkReceiver linkReceiver, LinkParamWrapper<UserDto> linkParamWrapper) {
        super(linkReceiver);
        this.linkParamWrapper = linkParamWrapper;
    }
    @Override
    public void execute() {
        UserDto userDto = linkParamWrapper.getDto();
        URI uri = linkReceiver.getUriForUser(linkParamWrapper.getUriInfo(), userDto);
        userDto.addLink(uri.toString(), linkParamWrapper.getRel());
    }
}
