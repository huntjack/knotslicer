package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.WithChildren;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.MemberWithSchedulesLinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@WithChildren
@ProcessAs(ProcessType.SCHEDULE)
@ApplicationScoped
public class MemberWithSchedulesLinkCreator implements LinkCreator<MemberDto> {
    @Override
    public LinkCommand<MemberDto> createLinkCommand(LinkReceiver linkReceiver, MemberDto memberDto, UriInfo uriInfo) {
        return new MemberWithSchedulesLinkCommand(linkReceiver, memberDto, uriInfo);
    }
}
