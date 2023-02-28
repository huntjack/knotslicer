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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

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
        EventDto eventDtoDummy = dtoCreator.createEventDto();
        eventDtoDummy.setEventId(1L);
        eventDtoDummy.setUserId(1L);
        PollDto pollDtoDummyOne = dtoCreator.createPollDto();
        pollDtoDummyOne.setPollId(1L);
        pollDtoDummyOne.setEventId(1L);
        PollDto pollDtoDummyTwo = dtoCreator.createPollDto();
        pollDtoDummyTwo.setPollId(2L);
        pollDtoDummyTwo.setEventId(1L);
        List<PollDto> pollDtos = new LinkedList<>();
        pollDtos.add(pollDtoDummyOne);
        pollDtos.add(pollDtoDummyTwo);
        eventDtoDummy.setPolls(pollDtos);

        Mockito.when(
                eventService.getWithChildren(anyLong()))
                .thenReturn(eventDtoDummy);
        EventDto eventResponseDto = target("/events/1/polls")
                .request()
                .get(EventDto.class);

        checkEvent(eventResponseDto, eventDtoDummy);
        List<PollDto> pollResponseDtos =
                eventResponseDto.getPolls();
        PollDto pollResponseDtoOne =
                pollResponseDtos.get(0);
        checkPolls(pollResponseDtoOne,
                pollDtoDummyOne.getPollId());
        PollDto pollResponseDtoTwo =
                pollResponseDtos.get(1);
        checkPolls(pollResponseDtoTwo,
                pollDtoDummyTwo.getPollId());
    }
    private void checkEvent(EventDto eventResponseDto, EventDto eventDtoDummy) {
        List<Link> eventDtoLinks = eventResponseDto.getLinks();
        Link selfLink = eventDtoLinks.get(0);
        assertEquals("self",
                selfLink.getRel());
        String eventId = eventDtoDummy
                .getEventId()
                .toString();
        assertTrue(selfLink
                        .getLink()
                        .contains("/events/" +
                                eventId),
                "EventDto's self link is incorrect.");
        Link userLink = eventDtoLinks.get(1);
        assertEquals("user",
                userLink.getRel());
        String userId = eventDtoDummy
                .getUserId()
                .toString();
        assertTrue(userLink
                        .getLink()
                        .contains("/users/" +
                                userId),
                "EventDto's user link is incorrect.");
    }
    private void checkPolls(PollDto pollResponseDto, Long pollId) {
        Link pollLink =
                pollResponseDto
                        .getLinks()
                        .get(0);
        assertEquals("poll",
                pollLink
                        .getRel());
        assertTrue(pollLink
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
