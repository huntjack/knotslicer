package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.MemberLinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@MemberLinkCreator
@ApplicationScoped
public class MemberLinkCreatorImpl implements LinkCreator<MemberDto> {
    @Override
    public LinkCommand createLinkCommand(LinkReceiver linkReceiver, MemberDto memberDto, UriInfo uriInfo) {
        return new MemberLinkCommand(linkReceiver, memberDto, uriInfo);
    }
}
