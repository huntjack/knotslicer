package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class PollWithPollAnswersLinkCommand extends PollLinkCommand {
    public PollWithPollAnswersLinkCommand(LinkReceiver linkReceiver, PollDto dto, UriInfo uriInfo) {
        super(linkReceiver, dto, uriInfo);
    }

    @Override
    public URI execute() {
        URI pollUri = super.execute();
        List<PollAnswerDto> pollAnswerDtos = dto.getPollAnswers();
        Long pollId = dto.getPollId();
        for(PollAnswerDto pollAnswerDto: pollAnswerDtos) {
            URI pollAnswerUri = linkReceiver.getUriForPollAnswer(
                    uriInfo.getBaseUriBuilder(),
                    pollAnswerDto.getPollAnswerId(),
                    pollId);
            pollAnswerDto.addLink(
                    pollAnswerUri.toString(),
                    "pollAnswer");
            URI memberUri = linkReceiver.getUriForMember(
                    uriInfo.getBaseUriBuilder(),
                    pollAnswerDto.getMemberId());
            pollAnswerDto.addLink(
                    memberUri.toString(),
                    "member");
        }
        return pollUri;
    }
}
