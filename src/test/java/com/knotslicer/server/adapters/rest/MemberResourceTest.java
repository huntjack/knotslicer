package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.*;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.*;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.ports.interactor.services.MemberService;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;

public class MemberResourceTest extends JerseyTest {
    @Mock
    private MemberService memberService;
    private LinkCreator<MemberDto> linkCreator;
    private LinkCreator<MemberDto> memberWithEventsLinkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;
    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new MemberLinkCreator();
        memberWithEventsLinkCreator = new MemberWithEventsLinkCreator();
        linkReceiver = new LinkReceiverImpl();
        return new ResourceConfig()
                .register(new MemberResourceImpl(
                        memberService,
                        linkCreator,
                        memberWithEventsLinkCreator,
                        linkReceiver));
    }
    @Test
    public void givenCorrectMemberId_whenGetWithEvents_thenLinksAreCorrect() {
        MemberDto memberDtoDummy = dtoCreator.createMemberDto();
        memberDtoDummy.setMemberId(1L);
        memberDtoDummy.setProjectId(1L);
        memberDtoDummy.setUserId(1L);
        EventDto eventDtoDummyOne = dtoCreator.createEventDto();
        eventDtoDummyOne.setEventId(1L);
        EventDto eventDtoDummyTwo = dtoCreator.createEventDto();
        eventDtoDummyTwo.setEventId(2L);
        List<EventDto> eventDtos = new LinkedList<>();
        eventDtos.add(eventDtoDummyOne);
        eventDtos.add(eventDtoDummyTwo);
        memberDtoDummy.setEvents(eventDtos);

        Mockito.when(
                        memberService.getWithEvents(anyLong()))
                .thenReturn(memberDtoDummy);
        MemberDto memberResponseDto = target("/members/1/events")
                .request()
                .get(MemberDto.class);

        checkMember(memberResponseDto, memberDtoDummy);
        List<EventDto> eventResponseDtos = memberResponseDto.getEvents();
        EventDto eventResponseDtoOne = eventResponseDtos.get(0);
        checkEvent(eventResponseDtoOne, eventDtoDummyOne);
        EventDto eventResponseDtoTwo = eventResponseDtos.get(1);
        checkEvent(eventResponseDtoTwo, eventDtoDummyTwo);
    }
    private void checkMember(MemberDto memberResponseDto, MemberDto memberDtoDummy) {
        List<Link> memberResponseDtoLinks = memberResponseDto.getLinks();
        Link memberLink = memberResponseDtoLinks.get(0);
        String memberId = memberDtoDummy
                .getMemberId()
                .toString();
        checkMemberLink(memberLink, "self", memberId);
        Link projectLink = memberResponseDtoLinks.get(1);
        String projectId = memberDtoDummy
                .getProjectId()
                .toString();
        checkProjectLink(projectLink, "project", projectId);
        Link userLink = memberResponseDtoLinks.get(2);
        String userId = memberDtoDummy
                .getUserId()
                .toString();
        checkUserLink(userLink, "user", userId);
    }
    private void checkMemberLink(Link memberLink, String rel, String memberId) {
        assertAll(
                "Member link should be correct.",
                () -> assertEquals(rel,
                        memberLink.getRel()),
                () -> assertTrue(memberLink
                                .getLink()
                                .contains("/members/" +
                                        memberId))
        );
    }
    private void checkProjectLink(Link projectLink, String rel, String projectId) {
        assertAll(
                "Project link should be correct.",
                () -> assertEquals(rel,
                        projectLink.getRel()),
                () -> assertTrue(projectLink
                                .getLink()
                                .contains("/projects/" +
                                        projectId))
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
    private void checkEvent(EventDto eventResponseDto, EventDto eventDtoDummy) {
        List<Link> eventDtoLinks = eventResponseDto.getLinks();
        Link eventLink = eventDtoLinks.get(0);
        String eventId = eventDtoDummy
                .getEventId()
                .toString();
        checkEventLink(eventLink, "event", eventId);
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

    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
