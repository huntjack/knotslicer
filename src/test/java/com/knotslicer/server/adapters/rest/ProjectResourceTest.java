package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ProjectLinkCreatorImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ProjectWithMembersLinkCreatorImpl;
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
        linkCreator = new ProjectLinkCreatorImpl();
        projectWithMembersLinkCreator = new ProjectWithMembersLinkCreatorImpl();
        linkReceiver = new LinkReceiverImpl();
        return new ResourceConfig()
                .register(new ProjectResourceImpl(
                        projectService,
                        linkCreator,
                        projectWithMembersLinkCreator,
                        linkReceiver));
    }
    @Test
    public void givenCorrectProjectId_whenGetWithMembers_thenLinksAreCorrect() {
        ProjectDto projectDto = dtoCreator.createProjectDto();
        projectDto.setProjectId(1L);
        projectDto.setUserId(1L);
        MemberDto memberDto1 = dtoCreator.createMemberDto();
        memberDto1.setMemberId(1L);
        memberDto1.setUserId(1L);
        MemberDto memberDto2 = dtoCreator.createMemberDto();
        memberDto2.setMemberId(2L);
        memberDto2.setUserId(2L);
        List<MemberDto> memberDtos = new LinkedList<>();
        memberDtos.add(memberDto1);
        memberDtos.add(memberDto2);
        projectDto.setMembers(memberDtos);
        Long projectId = projectDto.getProjectId();

        Mockito.when(
                projectService.getWithChildren(projectId))
                .thenReturn(projectDto);

        ProjectDto projectResponseDto = target("/projects/1/members")
                .request()
                .get(ProjectDto.class);
        checkProject(projectResponseDto, projectDto.getProjectId(), projectDto.getUserId());

        MemberDto memberResponseDtoOne =
                projectResponseDto
                        .getMembers()
                        .get(0);
        checkMember(memberResponseDtoOne, memberDto1.getMemberId(), memberDto1.getUserId());

        MemberDto memberResponseDtoTwo =
                projectResponseDto
                        .getMembers()
                        .get(1);
        checkMember(memberResponseDtoTwo, memberDto2.getMemberId(), memberDto2.getUserId());
    }
    private void checkProject(ProjectDto projectResponseDto, Long projectId, Long userId) {
        List<Link> listOfProjectDtoLinks = projectResponseDto.getLinks();
        Link projectSelfLink = listOfProjectDtoLinks.get(0);
        assertEquals("self",
                projectSelfLink.getRel());
        assertTrue(projectSelfLink
                .getLink()
                .contains("/projects/" +
                        projectId.toString()),
                "ProjectDto's self link is incorrect.");
        Link projectUserLink = listOfProjectDtoLinks.get(1);
        assertEquals("user",
                projectUserLink.getRel());
        assertTrue(projectUserLink
                .getLink()
                .contains("/users/" +
                        userId.toString()),
                "ProjectDto's user link is incorrect.");
    }
    private void checkMember(MemberDto memberResponseDto, Long memberId, Long userId) {
        Link memberResponseDtoMemberLink =
                memberResponseDto
                        .getLinks()
                        .get(0);
        assertEquals("member",
                memberResponseDtoMemberLink
                        .getRel());
        assertTrue(memberResponseDtoMemberLink
                .getLink()
                .contains("/members/" +
                        memberId.toString()),
                "MemberDto's member link is incorrect.");
        Link memberResponseDtoUserLink =
                memberResponseDto
                        .getLinks()
                        .get(1);
        assertEquals("user",
                memberResponseDtoUserLink
                        .getRel());
        assertTrue(memberResponseDtoUserLink
                .getLink()
                .contains("/users/" +
                        userId.toString()),
                "MemberDto's user link is incorrect");
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
