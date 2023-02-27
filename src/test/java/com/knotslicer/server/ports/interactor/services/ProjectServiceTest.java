package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
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
    private ChildWithOneRequiredParentDao<Project, User> projectDao;
    @Mock
    private ChildWithTwoParentsDao<Member,User,Project> memberDao;
    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        projectService = new ProjectServiceImpl(entityDtoMapper, projectDao, memberDao);
    }
    @Test
    public void givenCorrectProjectId_whenGetWithChildren_thenReturnProjectDtoWithMemberDtos() {
        Project project = entityCreator.createProject();
        project.setProjectId(1L);
        project.setProjectName("project1");
        project.setProjectDescription("project1 description");
        ProjectImpl projectImpl = (ProjectImpl) project;
        Member memberOne = entityCreator.createMember();
        memberOne.setMemberId(1L);
        memberOne.setName("member1");
        memberOne.setRole("member1 role");
        memberOne.setRoleDescription("member1 role description");
        MemberImpl memberImpl1 = (MemberImpl) memberOne;
        UserImpl userImpl1 = (UserImpl) entityCreator.createUser();
        userImpl1.addMember(memberImpl1);
        projectImpl.addMember(memberImpl1);
        Member memberTwo = entityCreator.createMember();
        memberTwo.setMemberId(2L);
        memberTwo.setName("member2");
        memberTwo.setRole("member2 role");
        memberTwo.setRoleDescription("member2 role description");
        MemberImpl memberImpl2 = (MemberImpl) memberTwo;
        UserImpl userImpl2 = (UserImpl) entityCreator.createUser();
        userImpl2.addMember(memberImpl2);
        projectImpl.addMember(memberImpl2);

        Mockito.when(
                memberDao.getSecondaryParentWithChildren(anyLong()))
                .thenReturn(Optional
                        .of(project));
        Long userId = 25L;
        Mockito.when(
                projectDao.getPrimaryParentId(anyLong()))
                .thenReturn(userId);

        ProjectDto projectDto =
                projectService.getWithChildren(7L);
        checkProject(project, projectDto, userId);

        List<MemberDto> memberDtos =
                projectDto.getMembers();
        MemberDto memberDtoOne = memberDtos.get(0);
        checkMember(memberOne, memberDtoOne);

        MemberDto memberDtoTwo = memberDtos.get(1);
        checkMember(memberTwo, memberDtoTwo);
    }
    private void checkProject(Project project, ProjectDto projectDto, Long userId) {
        assertEquals(project.getProjectId(),
                projectDto.getProjectId());
        assertEquals(project.getProjectName(),
                projectDto.getProjectName());
        assertEquals(project.getProjectDescription(),
                projectDto.getProjectDescription());
        assertEquals(userId,
                projectDto.getUserId());
    }
    private void checkMember(Member member, MemberDto memberDto) {
        assertEquals(member.getMemberId(),
                memberDto.getMemberId());
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
