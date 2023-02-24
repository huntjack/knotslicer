package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.EventLinkCreatorImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.EventWithPollsLinkCreatorImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.ports.interactor.services.ParentService;
import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventResourceTest extends JerseyTest {
    @Mock
    private ParentService<EventDto> eventService;
    private LinkCreator<EventDto> linkCreator;
    private LinkCreator<EventDto> eventWithPollsLinkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;
    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new EventLinkCreatorImpl();
        eventWithPollsLinkCreator = new EventWithPollsLinkCreatorImpl();
        linkReceiver = new LinkReceiverImpl();
        return new ResourceConfig()
                .register(new EventResourceImpl(
                        eventService,
                        linkCreator,
                        eventWithPollsLinkCreator,
                        linkReceiver));
    }
    @Test
    public void givenCorrectEventId_whenGetWithChildren_thenLinksAreCorrect() {
        EventDto eventDto = dtoCreator.createEventDto();
        eventDto.setEventId(1L);
        eventDto.setUserId(1L);
        PollDto pollDtoOne = dtoCreator.createPollDto();
        pollDtoOne.setPollId(1L);
        pollDtoOne.setEventId(1L);
        PollDto pollDtoTwo = dtoCreator.createPollDto();
        pollDtoTwo.setPollId(2L);
        pollDtoTwo.setEventId(1L);
        List<PollDto> pollDtos = new LinkedList<>();
        pollDtos.add(pollDtoOne);
        pollDtos.add(pollDtoTwo);
        eventDto.setPollDtos(pollDtos);
        Long eventId = eventDto.getEventId();

        Mockito.when(
                eventService.getWithChildren(eventId))
                .thenReturn(eventDto);

        EventDto eventResponseDto = target("/events/1/polls")
                .request()
                .get(EventDto.class);
        Long userId = eventDto.getUserId();
        checkEvent(eventResponseDto,
                eventId,
                userId);

        PollDto pollResponseDtoOne =
                eventResponseDto
                        .getPollDtos()
                        .get(0);
        checkPolls(pollResponseDtoOne,
                pollDtoOne.getPollId());

        PollDto pollResponseDtoTwo =
                eventResponseDto
                        .getPollDtos()
                        .get(1);
        checkPolls(pollResponseDtoTwo,
                pollDtoTwo.getPollId());
    }
    private void checkEvent(EventDto eventResponseDto, Long eventId, Long userId) {
        List<Link> eventDtoLinks = eventResponseDto.getLinks();
        Link eventSelfLink = eventDtoLinks.get(0);
        assertEquals("self",
                eventSelfLink.getRel());
        assertTrue(eventSelfLink
                        .getLink()
                        .contains("/events/" +
                                eventId.toString()),
                "EventDto's self link is incorrect.");
        Link eventUserLink = eventDtoLinks.get(1);
        assertEquals("user",
                eventUserLink.getRel());
        assertTrue(eventUserLink
                        .getLink()
                        .contains("/users/" +
                                userId.toString()),
                "EventDto's user link is incorrect.");
    }
    private void checkPolls(PollDto pollResponseDto, Long pollId) {
        Link pollDtoLink =
                pollResponseDto
                        .getLinks()
                        .get(0);
        assertEquals("poll",
                pollDtoLink
                        .getRel());
        assertTrue(pollDtoLink
                .getLink()
                .contains("/polls/" +
                        pollId.toString()),
                "PollDto's poll link is incorrect.");
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
