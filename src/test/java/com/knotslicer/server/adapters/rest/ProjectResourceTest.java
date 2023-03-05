package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ProjectLinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ProjectWithMembersLinkCreator;
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

public class ProjectResourceTest extends JerseyTest {
    @Mock
    private ParentService<ProjectDto> projectService;
    private LinkCreator<ProjectDto> linkCreator;
    private LinkCreator<ProjectDto> projectWithMembersLinkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;
    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new ProjectLinkCreator();
        projectWithMembersLinkCreator = new ProjectWithMembersLinkCreator();
        linkReceiver = new LinkReceiverImpl();
        return new ResourceConfig()
                .register(new ProjectResourceImpl(
                        projectService,
                        linkCreator,
                        projectWithMembersLinkCreator,
                        linkReceiver));
    }
    @Test
    public void givenCorrectProjectId_whenGetWithChildren_thenLinksAreCorrect() {
        ProjectDto projectDtoDummy = dtoCreator.createProjectDto();
        projectDtoDummy.setProjectId(1L);
        projectDtoDummy.setUserId(1L);
        MemberDto memberDtoDummyOne = dtoCreator.createMemberDto();
        memberDtoDummyOne.setMemberId(1L);
        memberDtoDummyOne.setUserId(1L);
        MemberDto memberDtoDummyTwo = dtoCreator.createMemberDto();
        memberDtoDummyTwo.setMemberId(2L);
        memberDtoDummyTwo.setUserId(2L);
        List<MemberDto> memberDtos = new LinkedList<>();
        memberDtos.add(memberDtoDummyOne);
        memberDtos.add(memberDtoDummyTwo);
        projectDtoDummy.setMembers(memberDtos);

        Mockito.when(
                projectService.getWithChildren(anyLong()))
                .thenReturn(projectDtoDummy);

        ProjectDto projectResponseDto = target("/projects/1/members")
                .request()
                .get(ProjectDto.class);

        checkProject(projectResponseDto, projectDtoDummy);
        List<MemberDto> memberResponseDtos =
                projectResponseDto.getMembers();
        MemberDto memberResponseDtoOne =
                memberResponseDtos.get(0);
        checkMember(memberResponseDtoOne, memberDtoDummyOne);
        MemberDto memberResponseDtoTwo =
                memberResponseDtos.get(1);
        checkMember(memberResponseDtoTwo, memberDtoDummyTwo);
    }
    private void checkProject(ProjectDto projectResponseDto, ProjectDto projectDtoDummy) {
        List<Link> projectDtoLinks = projectResponseDto.getLinks();
        Link selfLink = projectDtoLinks.get(0);
        assertEquals("self",
                selfLink.getRel());
        String projectId = projectDtoDummy
                .getProjectId()
                .toString();
        assertTrue(selfLink
                .getLink()
                .contains("/projects/" +
                        projectId),
                "ProjectDto's self link is incorrect.");
        Link userLink = projectDtoLinks.get(1);
        assertEquals("user",
                userLink.getRel());
        String userId = projectDtoDummy
                .getUserId()
                .toString();
        assertTrue(userLink
                .getLink()
                .contains("/users/" +
                        userId),
                "ProjectDto's user link is incorrect.");
    }
    private void checkMember(MemberDto memberResponseDto, MemberDto memberDtoDummy) {
        Link memberLink =
                memberResponseDto
                        .getLinks()
                        .get(0);
        assertEquals("member",
                memberLink
                        .getRel());
        String memberId = memberDtoDummy
                .getMemberId()
                .toString();
        assertTrue(memberLink
                .getLink()
                .contains("/members/" +
                        memberId),
                "MemberDto's member link is incorrect.");
        Link userLink =
                memberResponseDto
                        .getLinks()
                        .get(1);
        assertEquals("user",
                userLink
                        .getRel());
        String userId = memberDtoDummy
                .getUserId()
                .toString();
        assertTrue(userLink
                .getLink()
                .contains("/users/" +
                        userId),
                "MemberDto's user link is incorrect");
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
