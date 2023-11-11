package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.EventWithMembersLinkCreator;
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
import static org.mockito.ArgumentMatchers.anyLong;

public class EventWithMembersResourceTest extends JerseyTest {
    @Mock
    private EventService eventService;
    private LinkCreator<EventDto> eventWithMembersLinkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;
    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        eventWithMembersLinkCreator = new EventWithMembersLinkCreator();
        linkReceiver = new LinkReceiverImpl();
        return new ResourceConfig()
                .register(new EventWithMembersResourceImpl(
                        eventService,
                        eventWithMembersLinkCreator,
                        linkReceiver));
    }
    @Test
    public void givenCorrectEventId_whenGetWithMembers_thenLinksAreCorrect() {
        EventDto eventDtoDummy = dtoCreator.createEventDto();
        eventDtoDummy.setEventId(1L);
        eventDtoDummy.setUserId(1L);
        MemberDto memberDtoDummyOne = dtoCreator.createMemberDto();
        memberDtoDummyOne.setMemberId(1L);
        memberDtoDummyOne.setUserId(1L);
        memberDtoDummyOne.setProjectId(1L);
        MemberDto memberDtoDummyTwo = dtoCreator.createMemberDto();
        memberDtoDummyTwo.setMemberId(2L);
        memberDtoDummyTwo.setUserId(2L);
        memberDtoDummyTwo.setProjectId(1L);
        List<MemberDto> memberDtos = new LinkedList<>();
        memberDtos.add(memberDtoDummyOne);
        memberDtos.add(memberDtoDummyTwo);
        eventDtoDummy.setMembers(memberDtos);

        Mockito.when(
                        eventService.getWithMembers(anyLong()))
                .thenReturn(eventDtoDummy);
        EventDto eventResponseDto = target("/events/1/members")
                .request()
                .get(EventDto.class);

        checkEvent(eventResponseDto, eventDtoDummy);
        List<MemberDto> memberResponseDtos =
                eventResponseDto.getMembers();
        MemberDto memberResponseDtoOne = memberResponseDtos.get(0);
        checkMember(memberResponseDtoOne, memberDtoDummyOne);
        MemberDto memberResponseDtoTwo = memberResponseDtos.get(1);
        checkMember(memberResponseDtoTwo, memberDtoDummyTwo);
    }
    private void checkEvent(EventDto eventResponseDto, EventDto eventDtoDummy) {
        List<Link> eventDtoLinks = eventResponseDto.getLinks();
        Link eventLink = eventDtoLinks.get(0);
        String eventId = eventDtoDummy
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
    private void checkMember(MemberDto memberResponseDto, MemberDto memberDtoDummy) {
        List<Link> memberResponseDtoLinks = memberResponseDto.getLinks();
        Link memberLink = memberResponseDtoLinks.get(0);
        String memberId = memberDtoDummy
                .getMemberId()
                .toString();
        checkMemberLink(memberLink, "member", memberId);
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
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
