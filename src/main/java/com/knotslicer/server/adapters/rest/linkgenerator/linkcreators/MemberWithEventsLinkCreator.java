package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.MemberWithEventsLinkCommand;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.WithChildren;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.MEMBER)
@WithChildren(ProcessType.EVENT)
@ApplicationScoped
public class MemberWithEventsLinkCreator implements LinkCreator<MemberDto> {
    @Override
    public LinkCommand<MemberDto> createLinkCommand(LinkReceiver linkReceiver, MemberDto memberDto, UriInfo uriInfo) {
        return new MemberWithEventsLinkCommand(linkReceiver, memberDto, uriInfo);
    }
}
