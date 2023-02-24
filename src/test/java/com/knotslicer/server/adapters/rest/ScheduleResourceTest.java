package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.MemberWithSchedulesLinkCreatorImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ScheduleLinkCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.ports.interactor.mappers.EntityNotFoundExceptionMapper;
import com.knotslicer.server.ports.interactor.services.ParentService;
import com.knotslicer.server.ports.interactor.services.Service;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

public class ScheduleResourceTest extends JerseyTest {
    @Mock
    private Service<ScheduleDto> scheduleService;
    @Mock
    private ParentService<MemberDto> memberService;
    private LinkCreator<ScheduleDto> linkCreator;
    private LinkCreator<MemberDto> memberWithSchedulesLinkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;

    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new ScheduleLinkCreatorImpl();
        memberWithSchedulesLinkCreator = new MemberWithSchedulesLinkCreatorImpl();
        linkReceiver = new LinkReceiverImpl();
        return new ResourceConfig()
                .register(new ScheduleResourceImpl(
                        scheduleService,
                        memberService,
                        linkCreator,
                        memberWithSchedulesLinkCreator,
                        linkReceiver))
                .register(new EntityNotFoundExceptionMapper(
                        new ErrorDtoFactoryImpl()));
    }
    @Test
    public void givenCorrectMemberId_whenGet_thenResponseOk() {
        ScheduleDto scheduleDto = dtoCreator.createScheduleDto();
        scheduleDto.setScheduleId(1L);
        scheduleDto.setMemberId(1L);
        Mockito.when(scheduleService.get(1L)).thenReturn(scheduleDto);
        Response response = target("/members/1/schedules/1")
                .request()
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
    }
    @Test
    public void givenIncorrectMemberId_whenGet_thenResponseNotFound() {
        ScheduleDto scheduleDto = dtoCreator.createScheduleDto();
        scheduleDto.setScheduleId(2L);
        scheduleDto.setMemberId(1L);
        Mockito.when(scheduleService.get(2L)).thenReturn(scheduleDto);
        Response response = target("/members/2/schedules/2")
                .request()
                .get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
    }
    @Test
    public void givenCorrectMemberId_whenGetParentWithAllChildren_thenLinksAreCorrect() {
        MemberDto memberDto = dtoCreator.createMemberDto();
        memberDto.setMemberId(1L);
        memberDto.setUserId(1L);
        memberDto.setProjectId(1L);
        ScheduleDto scheduleDto1 = dtoCreator.createScheduleDto();
        scheduleDto1.setScheduleId(1L);
        scheduleDto1.setMemberId(1L);
        ScheduleDto scheduleDto2 = dtoCreator.createScheduleDto();
        scheduleDto2.setScheduleId(2L);
        scheduleDto2.setMemberId(1L);
        List<ScheduleDto> scheduleDtos = new LinkedList<>();
        scheduleDtos.add(scheduleDto1);
        scheduleDtos.add(scheduleDto2);
        memberDto.setSchedules(scheduleDtos);
        Long memberId = memberDto.getMemberId();

        Mockito.when(
                memberService.getWithChildren(memberId))
                .thenReturn(memberDto);

        MemberDto memberResponseDto = target("/members/1/schedules")
                .request()
                .get(MemberDto.class);
        checkMember(memberResponseDto,
                memberDto.getMemberId(),
                memberDto.getProjectId(),
                memberDto.getUserId());

        ScheduleDto scheduleResponseDtoOne =
                memberResponseDto
                        .getSchedules()
                        .get(0);
        checkSchedule(scheduleResponseDtoOne,
                scheduleDto1.getScheduleId(),
                scheduleDto1.getMemberId());

        ScheduleDto scheduleResponseDtoTwo =
                memberResponseDto
                        .getSchedules()
                        .get(1);
        checkSchedule(scheduleResponseDtoTwo,
                scheduleDto2.getScheduleId(),
                scheduleDto2.getMemberId());
    }
    private void checkMember(MemberDto memberResponseDto, Long memberId, Long projectId, Long userId) {
        List<Link> listOfMemberDtoLinks = memberResponseDto.getLinks();
        Link memberSelfLink = listOfMemberDtoLinks.get(0);
        assertEquals("self",
                memberSelfLink.getRel());
        assertTrue(memberSelfLink
                .getLink()
                .contains("/members/" +
                        memberId.toString()),
                "MemberDto's self link is incorrect.");
        Link memberProjectLink = listOfMemberDtoLinks.get(1);
        assertEquals("project",
                memberProjectLink.getRel());
        assertTrue(memberProjectLink
                .getLink()
                .contains("/projects/" +
                        projectId.toString()),
                "MemberDto's project link is incorrect.");
        Link memberUserLink = listOfMemberDtoLinks.get(2);
        assertEquals("user",
                memberUserLink.getRel());
        assertTrue(memberUserLink
                .getLink()
                .contains("/users/" +
                        userId.toString()),
                "MemberDto's user link is incorrect.");
    }
    private void checkSchedule(ScheduleDto scheduleResponseDto, Long scheduleId, Long memberId) {
        Link scheduleDtoLink =
                scheduleResponseDto
                        .getLinks()
                        .get(0);
        assertEquals("schedule",
                scheduleDtoLink
                        .getRel());
        assertTrue(scheduleDtoLink
                .getLink()
                .contains("/members/" +
                        memberId.toString() +
                        "/schedules/" +
                        scheduleId.toString()),
                "ScheduleDto's schedule link is incorrect.");
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
