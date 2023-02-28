package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.EntityCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
public class UserWithChildrenServiceTest {

    private UserWithChildrenService userWithChildrenService;
    private EntityCreator entityCreator = new EntityCreatorImpl();
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private EntityDtoMapper entityDtoMapper = new EntityDtoMapperImpl(entityCreator, dtoCreator);
    @Mock
    private ChildWithOneRequiredParentDao<Project, User> projectDao;
    @Mock
    private ChildWithTwoParentsDao<Member,User,Project> memberDao;
    @Mock
    private ChildWithOneRequiredParentDao<Event, User> eventDao;
    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        userWithChildrenService = new UserWithChildrenServiceImpl(
                entityDtoMapper,
                projectDao,
                memberDao,
                eventDao);
    }
    @Test
    public void givenCorrectUserId_whenGetWithProjects_thenReturnUserLightDtoWithProjectDtos() {
        User user = createTestUser();
        Project projectOne = entityCreator.createProject();
        projectOne.setProjectId(1L);
        projectOne.setProjectName("Project One Name");
        projectOne.setProjectDescription("Project One Description");
        UserImpl userImpl = (UserImpl) user;
        ProjectImpl projectOneImpl = (ProjectImpl) projectOne;
        userImpl.addProject(projectOneImpl);
        Project projectTwo = entityCreator.createProject();
        projectTwo.setProjectId(2L);
        projectTwo.setProjectName("Project Two Name");
        projectTwo.setProjectDescription("Project Two Description");
        ProjectImpl projectTwoImpl = (ProjectImpl) projectTwo;
        userImpl.addProject(projectTwoImpl);

        Mockito.when(
                projectDao.getPrimaryParentWithChildren(anyLong()))
                .thenReturn(
                        Optional.of(user));
        Long userId = user.getUserId();
        UserLightDto userLightDto =
                userWithChildrenService.getWithProjects(userId);

        checkUserLightDto(user, userLightDto);
        List<ProjectDto> projectDtos = userLightDto.getProjects();
        ProjectDto projectDtoOne = projectDtos.get(0);
        checkProjectDto(projectOne, projectDtoOne);
        ProjectDto projectDtoTwo = projectDtos.get(1);
        checkProjectDto(projectTwo, projectDtoTwo);
    }
    private User createTestUser() {
        User user = entityCreator.createUser();
        user.setUserId(1L);
        user.setEmail("example@mail.com");
        user.setUserName("testUser");
        user.setUserDescription("Test User Description");
        user.setTimeZone(
                ZoneId.of("America/Los_Angeles"));
        return user;
    }
    private void checkUserLightDto(User user, UserLightDto userLightDto) {
        assertEquals(user.getUserId(), userLightDto.getUserId());
        assertEquals(user.getUserName(), userLightDto.getUserName());
        assertEquals(user.getUserDescription(), userLightDto.getUserDescription());
        assertEquals(user.getTimeZone(), userLightDto.getTimeZone());
    }
    private void checkProjectDto(Project project, ProjectDto projectDto) {
        assertEquals(project.getProjectId(), projectDto.getProjectId());
        assertEquals(project.getProjectName(), projectDto.getProjectName());
        assertEquals(project.getProjectDescription(), projectDto.getProjectDescription());
    }
    @Test
    public void givenCorrectUserId_whenGetWithEvents_thenReturnUserLightDtoWithEventDtos() {
        User user = createTestUser();
        Event eventOne = entityCreator.createEvent();
        eventOne.setEventId(1L);
        eventOne.setEventName("EventOne Name");
        eventOne.setSubject("EventOne Subject");
        eventOne.setEventDescription("EventOne Description");
        UserImpl userImpl = (UserImpl) user;
        EventImpl eventImplOne = (EventImpl) eventOne;
        userImpl.addEvent(eventImplOne);
        Event eventTwo = entityCreator.createEvent();
        eventTwo.setEventId(2L);
        eventTwo.setEventName("EventTwo Name");
        eventTwo.setSubject("EventTwo Subject");
        eventTwo.setEventDescription("EventTwo Description");
        EventImpl eventImplTwo = (EventImpl) eventTwo;
        userImpl.addEvent(eventImplTwo);

        Mockito.when(eventDao.getPrimaryParentWithChildren(anyLong()))
                .thenReturn(
                        Optional.of(user));
        Long userId = user.getUserId();
        UserLightDto userLightDto =
                userWithChildrenService.getWithEvents(userId);

        checkUserLightDto(user, userLightDto);
        List<EventDto> eventDtos = userLightDto.getEvents();
        EventDto eventDtoOne = eventDtos.get(0);
        checkEventDto(eventOne, eventDtoOne);
        EventDto eventDtoTwo = eventDtos.get(1);
        checkEventDto(eventTwo, eventDtoTwo);
    }
    private void checkEventDto(Event event, EventDto eventDto) {
        assertEquals(event.getEventId(),
                eventDto.getEventId());
        assertEquals(event.getEventName(),
                eventDto.getEventName());
        assertEquals(event.getSubject(),
                eventDto.getSubject());
        assertEquals(event.getEventDescription(),
                eventDto.getEventDescription());
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
