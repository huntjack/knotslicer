package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.MemberLinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.MEMBER)
@Default
@ApplicationScoped
public class MemberLinkCreator implements LinkCreator<MemberDto> {
    @Override
    public LinkCommand<MemberDto> createLinkCommand(LinkReceiver linkReceiver, MemberDto memberDto, UriInfo uriInfo) {
        return new MemberLinkCommand(linkReceiver, memberDto, uriInfo);
    }
}
