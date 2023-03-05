package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.UserWithMembersLinkCreator;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;

public class UserWithMembersResourceTest extends JerseyTest {
    @Mock
    UserWithChildrenService userWithMembersService;
    private LinkCreator<UserLightDto> linkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;
    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new UserWithMembersLinkCreator();
        linkReceiver = new LinkReceiverImpl();
        return new ResourceConfig()
                .register(new UserWithMembersResource(
                        userWithMembersService,
                        linkCreator,
                        linkReceiver));
    }
    @Test
    public void givenCorrectUserId_whenGetUserWithChildren_thenLinksAreCorrect() {
        UserLightDto userLightDtoDummy = dtoCreator.createUserLightDto();
        userLightDtoDummy.setUserId(1L);
        MemberDto memberDtoDummyOne = dtoCreator.createMemberDto();
        memberDtoDummyOne.setMemberId(1L);
        memberDtoDummyOne.setProjectId(1L);
        MemberDto memberDtoDummyTwo = dtoCreator.createMemberDto();
        memberDtoDummyTwo.setMemberId(2L);
        memberDtoDummyTwo.setProjectId(2L);
        List<MemberDto> memberDtos = new LinkedList<>();
        memberDtos.add(memberDtoDummyOne);
        memberDtos.add(memberDtoDummyTwo);
        userLightDtoDummy.setMembers(memberDtos);

        Mockito.when(
                        userWithMembersService.getUserWithChildren(anyLong()))
                .thenReturn(userLightDtoDummy);
        UserLightDto userResponseDto = target("/users/1/members")
                .request()
                .get(UserLightDto.class);

        checkUser(userResponseDto, userLightDtoDummy);
        List<MemberDto> memberResponseDtos =
                userResponseDto.getMembers();
        MemberDto memberResponseDtoOne = memberResponseDtos.get(0);
        checkMember(memberResponseDtoOne, memberDtoDummyOne);
        MemberDto memberResponseDtoTwo = memberResponseDtos.get(1);
        checkMember(memberResponseDtoTwo, memberDtoDummyTwo);
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
    private void checkMember(MemberDto memberResponseDto, MemberDto memberDtoDummy) {
        List<Link> memberResponseDtoLinks = memberResponseDto.getLinks();
        Link memberLink = memberResponseDtoLinks.get(0);
        assertEquals("member",
                memberLink.getRel());
        String memberId = memberDtoDummy
                .getMemberId()
                .toString();
        assertTrue(memberLink
                .getLink()
                .contains("/members/" +
                        memberId),
                "MemberDto's member link is incorrect.");
        Link projectLink = memberResponseDtoLinks.get(1);
        assertEquals("project",
                projectLink.getRel());
        Long projectId = memberDtoDummy.getProjectId();
        assertTrue(projectLink
                .getLink()
                .contains("/projects/" +
                        projectId),
                "ProjectDto's project link is incorrect.");
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
