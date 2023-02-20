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

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleResourceTest extends JerseyTest {
    @Mock
    private Service<ScheduleDto> scheduleService;
    @Mock
    private ParentService<MemberDto> memberService;
    private LinkCreator<ScheduleDto> linkCreator;
    private LinkCreator<MemberDto> memberWithSchedulesLinkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator;
    private AutoCloseable closeable;

    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new ScheduleLinkCreatorImpl();
        memberWithSchedulesLinkCreator = new MemberWithSchedulesLinkCreatorImpl();
        linkReceiver = new LinkReceiverImpl();
        dtoCreator = new DtoCreatorImpl();
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
    public void givenIncorrectMemberId_whenGet_thenResponseInternalServerError() {
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
    public void givenCorrectMemberId_whenGetParentWithAllChildren_thenScheduleLinksAreOkay() {

    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
