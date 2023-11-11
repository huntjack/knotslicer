package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiverImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.UserWithProjectsLinkCreator;
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

public class UserWithProjectsResourceTest extends JerseyTest {
    @Mock
    UserWithChildrenService userWithProjectsService;
    private LinkCreator<UserLightDto> linkCreator;
    private LinkReceiver linkReceiver;
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private AutoCloseable closeable;
    @Override
    protected Application configure() {
        closeable = MockitoAnnotations.openMocks(this);
        linkCreator = new UserWithProjectsLinkCreator();
        linkReceiver = new LinkReceiverImpl();
        return new ResourceConfig()
                .register(new UserWithProjectsResource(
                        userWithProjectsService,
                        linkCreator,
                        linkReceiver));
    }
    @Test
    public void givenCorrectUserId_whenGetUserWithChildren_thenLinksAreCorrect() {
        UserLightDto userLightDtoDummy = dtoCreator.createUserLightDto();
        userLightDtoDummy.setUserId(1L);
        ProjectDto projectDtoDummyOne = dtoCreator.createProjectDto();
        projectDtoDummyOne.setProjectId(1L);
        ProjectDto projectDtoDummyTwo = dtoCreator.createProjectDto();
        projectDtoDummyTwo.setProjectId(2L);
        List<ProjectDto> projectDtos = new LinkedList<>();
        projectDtos.add(projectDtoDummyOne);
        projectDtos.add(projectDtoDummyTwo);
        userLightDtoDummy.setProjects(projectDtos);

        Mockito.when(
                userWithProjectsService.getUserWithChildren(anyLong()))
                .thenReturn(userLightDtoDummy);
        UserLightDto userResponseDto = target("/users/1/projects")
                .request()
                .get(UserLightDto.class);

        checkUser(userResponseDto, userLightDtoDummy);
        List<ProjectDto> projectResponseDtos =
                userResponseDto.getProjects();
        ProjectDto projectResponseDtoOne = projectResponseDtos.get(0);
        checkProject(projectResponseDtoOne, projectDtoDummyOne);
        ProjectDto projectResponseDtoTwo = projectResponseDtos.get(1);
        checkProject(projectResponseDtoTwo, projectDtoDummyTwo);
    }
    private void checkUser(UserLightDto userResponseDto, UserLightDto userDtoDummy) {
        List<Link> userDtoLinks = userResponseDto.getLinks();
        Link userLink = userDtoLinks.get(0);
        String userId = userDtoDummy
                .getUserId()
                .toString();
        checkUserLink(userLink, "self", userId);
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
    private void checkProject(ProjectDto projectResponseDto, ProjectDto projectDtoDummy) {
        List<Link> projectDtoLinks = projectResponseDto.getLinks();
        Link projectLink = projectDtoLinks.get(0);
        String projectId = projectDtoDummy
                .getProjectId()
                .toString();
        checkProjectLink(projectLink, "project", projectId);
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
