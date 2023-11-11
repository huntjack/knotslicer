package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.EventLinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.EventWithPollsLinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.ports.interactor.services.EventService;
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
    private EventService eventService;
    private LinkCreator<EventDto> linkCreator;
    private LinkCreator<EventDto> eventWithPollsLinkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;
    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new EventLinkCreator();
        eventWithPollsLinkCreator = new EventWithPollsLinkCreator();
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
        checkPoll(pollResponseDtoOne, pollDtoDummyOne);
        PollDto pollResponseDtoTwo =
                pollResponseDtos.get(1);
        checkPoll(pollResponseDtoTwo, pollDtoDummyTwo);
    }
    private void checkEvent(EventDto eventResponseDto, EventDto eventDtoDummy) {
        List<Link> eventDtoLinks = eventResponseDto.getLinks();
        Link eventLink = eventDtoLinks.get(0);
        String eventId =
                eventDtoDummy
                        .getEventId()
                        .toString();
        checkEventLink(eventLink, "self", eventId);
        Link userLink = eventDtoLinks.get(1);
        String userId = eventDtoDummy
                .getUserId()
                .toString();
        checkUserLink(userLink, "user", userId);
    }
    private void checkEventLink(Link eventLink, String rel, String eventId) {
        assertAll(
                "Event link should be correct.",
                () -> assertEquals(rel,
                        eventLink.getRel()),
                () -> assertTrue(eventLink
                        .getLink()
                        .contains("/events/" +
                                eventId))
        );
    }
    private void checkUserLink(Link userLink, String rel, String userId) {
        assertAll(
                "User link should be correct.",
                () -> assertEquals(rel,
                        userLink.getRel()),
                () -> assertTrue(userLink
                        .getLink()
                        .contains("/users/" +
                                userId))
        );
    }
    private void checkPoll(PollDto pollResponseDto, PollDto pollDtoDummy) {
        Link pollLink =
                pollResponseDto
                        .getLinks()
                        .get(0);
        String pollId =
                pollDtoDummy
                        .getPollId()
                        .toString();
        checkPollLink(pollLink, "poll", pollId);
    }
    private void checkPollLink(Link pollLink, String rel, String pollId) {
        assertAll(
                "Poll link should be correct.",
                () -> assertEquals(rel,
                        pollLink
                                .getRel()),
                () -> assertTrue(pollLink
                        .getLink()
                        .contains("/polls/" +
                                pollId.toString()))
        );
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
