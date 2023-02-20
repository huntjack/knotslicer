package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.entitygateway.ProjectDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.EntityCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

public class ProjectServiceTest {
    private ParentService<ProjectDto> projectService;
    private EntityCreator entityCreator = new EntityCreatorImpl();
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private EntityDtoMapper entityDtoMapper = new EntityDtoMapperImpl(entityCreator, dtoCreator);
    @Mock
    private ProjectDao projectDao;
    @Mock
    private MemberDao memberDao;
    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        projectService = new ProjectServiceImpl(entityDtoMapper, projectDao, memberDao);
    }
    @Test
    public void givenCorrectProjectId_whenGetWithChildren_thenReturnProjectDtoWithMemberDtos() {
        Project project = entityCreator.createProject();
        project.setProjectName("project1");
        project.setProjectDescription("project1 description");
        ProjectImpl projectImpl = (ProjectImpl) project;
        Member member1 = entityCreator.createMember();
        member1.setName("member1");
        member1.setRole("member1 role");
        member1.setRoleDescription("member1 role description");
        MemberImpl memberImpl1 = (MemberImpl) member1;
        UserImpl userImpl1 = (UserImpl) entityCreator.createUser();
        userImpl1.addMember(memberImpl1);
        projectImpl.addMember(memberImpl1);
        Member member2 = entityCreator.createMember();
        member2.setName("member2");
        member2.setRole("member2 role");
        member2.setRoleDescription("member2 role description");
        MemberImpl memberImpl2 = (MemberImpl) member2;
        UserImpl userImpl2 = (UserImpl) entityCreator.createUser();
        userImpl2.addMember(memberImpl2);
        projectImpl.addMember(memberImpl2);

        Mockito.when(
                memberDao.getProjectWithMembers(anyLong()))
                .thenReturn(Optional
                        .ofNullable(project));
        Long userId = 25L;
        Mockito.when(
                projectDao.getPrimaryParentId(anyLong()))
                .thenReturn(userId);

        ProjectDto projectDto =
                projectService.getWithChildren(7L);
        checkProject(projectDto, project, userId);

        List<MemberDto> memberDtos =
                projectDto.getMembers();
        MemberDto memberDto1 = memberDtos.get(0);
        checkMember(memberDto1, member1);

        MemberDto memberDto2 = memberDtos.get(1);
        checkMember(memberDto2, member2);
    }
    private void checkProject(ProjectDto projectDto, Project project, Long userId) {
        assertEquals(project.getProjectName(),
                projectDto.getProjectName());
        assertEquals(project.getProjectDescription(),
                projectDto.getProjectDescription());
        assertEquals(userId,
                projectDto.getUserId());
    }
    private void checkMember(MemberDto memberDto, Member member) {
        assertEquals(member.getName(),
                memberDto.getName());
        assertEquals(member.getRole(),
                memberDto.getRole());
        assertEquals(member.getRoleDescription(),
                memberDto.getRoleDescription());
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
