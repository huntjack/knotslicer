package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.MemberWithSchedulesLinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ScheduleLinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.ports.interactor.mappers.EntityNotFoundExceptionMapper;
import com.knotslicer.server.ports.interactor.services.MemberService;
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
import static org.mockito.ArgumentMatchers.*;

public class ScheduleResourceTest extends JerseyTest {
    @Mock
    private Service<ScheduleDto> scheduleService;
    @Mock
    private MemberService memberService;
    private LinkCreator<ScheduleDto> linkCreator;
    private LinkCreator<MemberDto> memberWithSchedulesLinkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;

    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new ScheduleLinkCreator();
        memberWithSchedulesLinkCreator = new MemberWithSchedulesLinkCreator();
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
        Mockito.when(scheduleService.get(anyLong()))
                .thenReturn(scheduleDto);
        Response response = target("/members/1/schedules/1")
                .request()
                .get();
        assertEquals(Response.Status.OK.getStatusCode(),
                response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE,
                response.getMediaType());
    }
    @Test
    public void givenIncorrectMemberId_whenGet_thenResponseNotFound() {
        ScheduleDto scheduleDto = dtoCreator.createScheduleDto();
        scheduleDto.setScheduleId(2L);
        scheduleDto.setMemberId(1L);
        Mockito.when(scheduleService.get(anyLong()))
                .thenReturn(scheduleDto);
        Response response = target("/members/2/schedules/2")
                .request()
                .get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE,
                response.getMediaType());
    }
    @Test
    public void givenCorrectMemberId_whenGetParentWithAllChildren_thenLinksAreCorrect() {
        MemberDto memberDtoDummy = dtoCreator.createMemberDto();
        memberDtoDummy.setMemberId(1L);
        memberDtoDummy.setUserId(1L);
        memberDtoDummy.setProjectId(1L);
        ScheduleDto scheduleDtoDummyOne = dtoCreator.createScheduleDto();
        scheduleDtoDummyOne.setScheduleId(1L);
        scheduleDtoDummyOne.setMemberId(1L);
        ScheduleDto scheduleDtoDummyTwo = dtoCreator.createScheduleDto();
        scheduleDtoDummyTwo.setScheduleId(2L);
        scheduleDtoDummyTwo.setMemberId(1L);
        List<ScheduleDto> scheduleDtos = new LinkedList<>();
        scheduleDtos.add(scheduleDtoDummyOne);
        scheduleDtos.add(scheduleDtoDummyTwo);
        memberDtoDummy.setSchedules(scheduleDtos);

        Mockito.when(
                memberService.getWithChildren(anyLong()))
                .thenReturn(memberDtoDummy);

        MemberDto memberResponseDto = target("/members/1/schedules")
                .request()
                .get(MemberDto.class);

        checkMember(memberResponseDto, memberDtoDummy);
        List<ScheduleDto> scheduleResponseDtos =
                memberResponseDto.getSchedules();
        ScheduleDto scheduleResponseDtoOne =
                scheduleResponseDtos.get(0);
        checkSchedule(scheduleResponseDtoOne, scheduleDtoDummyOne);
        ScheduleDto scheduleResponseDtoTwo =
                scheduleResponseDtos.get(1);
        checkSchedule(scheduleResponseDtoTwo, scheduleDtoDummyTwo);
    }
    private void checkMember(MemberDto memberResponseDto, MemberDto memberDtoDummy) {
        List<Link> memberDtoLinks = memberResponseDto.getLinks();
        Link memberLink = memberDtoLinks.get(0);
        String memberId = memberDtoDummy
                .getMemberId()
                .toString();
        checkMemberLink(memberLink, "self", memberId);
        Link projectLink = memberDtoLinks.get(1);
        String projectId = memberDtoDummy
                .getProjectId()
                .toString();
        checkProjectLink(projectLink, "project", projectId);
        Link userLink = memberDtoLinks.get(2);
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
    private void checkSchedule(ScheduleDto scheduleResponseDto, ScheduleDto scheduleDtoDummy) {
        Link scheduleLink =
                scheduleResponseDto
                        .getLinks()
                        .get(0);
        String memberId = scheduleDtoDummy
                .getMemberId()
                .toString();
        String scheduleId = scheduleDtoDummy
                .getScheduleId()
                .toString();
        checkScheduleLink(scheduleLink, "schedule", memberId, scheduleId);
    }
    private void checkScheduleLink(Link scheduleLink, String rel, String memberId, String scheduleId) {
        assertAll(
                "Schedule link should be correct.",
                () -> assertEquals(rel,
                        scheduleLink.getRel()),
                () -> assertTrue(scheduleLink
                        .getLink()
                        .contains("/members/" +
                                memberId +
                                "/schedules/" +
                                scheduleId))
        );
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
