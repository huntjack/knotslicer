package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.UserWithEventsLinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.ports.interactor.services.UserWithChildrenService;
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
import static org.mockito.ArgumentMatchers.anyLong;

public class UserWithEventsResourceTest extends JerseyTest {
    @Mock
    UserWithChildrenService userWithProjectsService;
    private LinkCreator<UserLightDto> linkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;
    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new UserWithEventsLinkCreator();
        linkReceiver = new LinkReceiverImpl();
        return new ResourceConfig()
                .register(new UserWithEventsResource(
                        userWithProjectsService,
                        linkCreator,
                        linkReceiver));
    }
    @Test
    public void givenCorrectUserId_whenGetUserWithChildren_thenLinksAreCorrect() {
        UserLightDto userLightDtoDummy = dtoCreator.createUserLightDto();
        userLightDtoDummy.setUserId(1L);
        EventDto eventDtoDummyOne = dtoCreator.createEventDto();
        eventDtoDummyOne.setEventId(1L);
        EventDto eventDtoDummyTwo = dtoCreator.createEventDto();
        eventDtoDummyTwo.setEventId(2L);
        List<EventDto> eventDtos = new LinkedList<>();
        eventDtos.add(eventDtoDummyOne);
        eventDtos.add(eventDtoDummyTwo);
        userLightDtoDummy.setEvents(eventDtos);

        Mockito.when(
                        userWithProjectsService.getUserWithChildren(anyLong()))
                .thenReturn(userLightDtoDummy);
        UserLightDto userResponseDto = target("/users/1/events")
                .request()
                .get(UserLightDto.class);

        checkUser(userResponseDto, userLightDtoDummy);
        List<EventDto> eventResponseDtos =
                userResponseDto.getEvents();
        EventDto eventResponseDtoOne = eventResponseDtos.get(0);
        checkEvent(eventResponseDtoOne, eventDtoDummyOne);
        EventDto eventResponseDtoTwo = eventResponseDtos.get(1);
        checkEvent(eventResponseDtoTwo, eventDtoDummyTwo);
    }
    private void checkUser(UserLightDto userResponseDto, UserLightDto userDtoDummy) {
        List<Link> userResponseDtoLinks = userResponseDto.getLinks();
        Link selfLink = userResponseDtoLinks.get(0);
        assertEquals("self",
                selfLink.getRel());
        String userId = userDtoDummy
                .getUserId()
                .toString();
        assertTrue(selfLink
                        .getLink()
                        .contains("/users/" +
                                userId),
                "UserDto's self link is incorrect.");
    }
    private void checkEvent(EventDto eventResponseDto, EventDto eventDtoDummy) {
        List<Link> eventResponseDtoLinks = eventResponseDto.getLinks();
        Link eventLink = eventResponseDtoLinks.get(0);
        assertEquals("event",
                eventLink.getRel());
        String eventId = eventDtoDummy
                .getEventId()
                .toString();
        assertTrue(eventLink
                        .getLink()
                        .contains("/events/" +
                                eventId),
                "EventDto's event link is incorrect.");
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
