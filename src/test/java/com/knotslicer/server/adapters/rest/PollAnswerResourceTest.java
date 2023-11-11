package com.knotslicer.server.adapters.rest;


import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.PollAnswerLinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.PollWithPollAnswersLinkCreator;
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
import static org.mockito.ArgumentMatchers.*;

public class PollAnswerResourceTest extends JerseyTest {
    @Mock
    private Service<PollAnswerDto> pollAnswerService;
    @Mock
    private ParentService<PollDto> pollService;
    private LinkCreator<PollAnswerDto> linkCreator;
    private LinkCreator<PollDto> pollWithPollAnswersLinkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;
    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new PollAnswerLinkCreator();
        pollWithPollAnswersLinkCreator = new PollWithPollAnswersLinkCreator();
        linkReceiver = new LinkReceiverImpl();
        return new ResourceConfig()
                .register(new PollAnswerResourceImpl(
                        pollAnswerService,
                        pollService,
                        linkCreator,
                        pollWithPollAnswersLinkCreator,
                        linkReceiver))
                .register(new EntityNotFoundExceptionMapper(
                        new ErrorDtoFactoryImpl()));
    }
    @Test
    public void givenCorrectPollId_whenGet_thenResponseOk(){
        PollAnswerDto pollAnswerDto = dtoCreator.createPollAnswerDto();
        pollAnswerDto.setPollAnswerId(1L);
        pollAnswerDto.setPollId(1L);
        pollAnswerDto.setMemberId(1L);
        Mockito.when(pollAnswerService.get(anyLong()))
                .thenReturn(pollAnswerDto);
        Response response = target("/polls/1/pollanswers/1")
                .request()
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
    }
    @Test
    public void givenIncorrectPollId_whenGet_thenResponseNotFound() {
        PollAnswerDto pollAnswerDto = dtoCreator.createPollAnswerDto();
        pollAnswerDto.setPollAnswerId(2L);
        pollAnswerDto.setPollId(1L);
        pollAnswerDto.setMemberId(2L);
        Mockito.when(pollAnswerService.get(anyLong()))
                .thenReturn(pollAnswerDto);
        Response response = target("/polls/2/pollanswers/2")
                .request()
                .get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
    }
    @Test
    public void givenCorrectPollId_whenGetParentWithAllChildren_thenLinksAreCorrect() {
        PollDto pollDtoDummy = dtoCreator.createPollDto();
        pollDtoDummy.setPollId(1L);
        pollDtoDummy.setEventId(1L);
        PollAnswerDto pollAnswerDtoDummyOne = dtoCreator.createPollAnswerDto();
        pollAnswerDtoDummyOne.setPollAnswerId(1L);
        pollAnswerDtoDummyOne.setPollId(1L);
        pollAnswerDtoDummyOne.setMemberId(1L);
        PollAnswerDto pollAnswerDtoDummyTwo = dtoCreator.createPollAnswerDto();
        pollAnswerDtoDummyTwo.setPollAnswerId(2L);
        pollAnswerDtoDummyTwo.setPollId(1L);
        pollAnswerDtoDummyTwo.setMemberId(2L);
        List<PollAnswerDto> pollAnswerDtos = new LinkedList<>();
        pollAnswerDtos.add(pollAnswerDtoDummyOne);
        pollAnswerDtos.add(pollAnswerDtoDummyTwo);
        pollDtoDummy.setPollAnswers(pollAnswerDtos);

        Mockito.when(pollService.getWithChildren(anyLong()))
                .thenReturn(pollDtoDummy);
        PollDto pollResponseDto = target("/polls/1/pollanswers")
                .request()
                .get(PollDto.class);

        checkPoll(pollResponseDto, pollDtoDummy);
        List<PollAnswerDto> pollAnswerResponseDtos =
                pollResponseDto.getPollAnswers();
        PollAnswerDto pollAnswerResponseDtoOne =
                pollAnswerResponseDtos.get(0);
        checkPollAnswer(pollAnswerResponseDtoOne, pollAnswerDtoDummyOne);
        PollAnswerDto pollAnswerResponseDtoTwo =
                pollAnswerResponseDtos.get(1);
        checkPollAnswer(pollAnswerResponseDtoTwo, pollAnswerDtoDummyTwo);
    }
    private void checkPoll(PollDto pollResponseDto, PollDto pollDtoDummy) {
        List<Link> pollDtoLinks = pollResponseDto.getLinks();
        Link pollLink = pollDtoLinks.get(0);
        String pollId = pollDtoDummy
                .getPollId()
                .toString();
        checkPollLink(pollLink, "self", pollId);
        Link eventLink = pollDtoLinks.get(1);

        String eventId = pollDtoDummy
                .getEventId()
                .toString();
        checkEventLink(eventLink, "event", eventId);
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
    private void checkPollAnswer(PollAnswerDto pollAnswerResponseDto, PollAnswerDto pollAnswerDummyDto) {
        List<Link> pollAnswerResponseLinks =
                pollAnswerResponseDto.getLinks();
        Link pollAnswerLink =
                pollAnswerResponseLinks.get(0);
        String pollId = pollAnswerDummyDto
                .getPollId()
                .toString();
        String pollAnswerId = pollAnswerDummyDto
                .getPollAnswerId()
                .toString();
        checkPollAnswerLink(pollAnswerLink, "pollAnswer", pollId, pollAnswerId);

        Link memberLink = pollAnswerResponseLinks.get(1);
        String memberId = pollAnswerDummyDto
                .getMemberId()
                .toString();
        checkMemberLink(memberLink, "member", memberId);
    }
    private void checkPollAnswerLink(Link pollAnswerLink, String rel, String pollId, String pollAnswerId) {
        assertAll(
                "PollAnswer link should be correct.",
                () -> assertEquals(rel,
                        pollAnswerLink.getRel()),
                () -> assertTrue(pollAnswerLink
                        .getLink()
                        .contains("/polls/" +
                                pollId +
                                "/pollanswers/" +
                                pollAnswerId))
        );
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
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
