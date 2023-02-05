package com.knotslicer.server;

import com.knotslicer.server.domain.MemberImpl;
import com.knotslicer.server.domain.ProjectImpl;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.domain.UserImpl;
import com.knotslicer.server.ports.interactor.EntityFactory;
import com.knotslicer.server.ports.interactor.EntityFactoryImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoFactory;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoFactoryImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapperImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityDtoMapperTest {
    EntityFactory entityFactory = new EntityFactoryImpl();
    DtoFactory dtoFactory = new DtoFactoryImpl();
    EntityDtoMapper entityDtoMapper = new EntityDtoMapperImpl(entityFactory, dtoFactory);
    @Test
    void testAddMembers() {
        ProjectImpl projectImpl = (ProjectImpl) entityFactory.createProject();
        MemberImpl expectedFirstMember = (MemberImpl) entityFactory.createMember();
        expectedFirstMember.setName("John");
        UserImpl firstMembersUser = (UserImpl) entityFactory.createUser();
        firstMembersUser.addMember(expectedFirstMember);
        MemberImpl expectedSecondMember = (MemberImpl) entityFactory.createMember();
        expectedSecondMember.setName("Sally");
        UserImpl secondMembersUser = (UserImpl) entityFactory.createUser();
        secondMembersUser.addMember(expectedSecondMember);
        projectImpl.addMember(expectedFirstMember);
        projectImpl.addMember(expectedSecondMember);
        ProjectDto actualProjectDto = dtoFactory.createProjectDto();
        actualProjectDto = entityDtoMapper.addMembers(actualProjectDto, projectImpl);
        MemberDto actualFirstMember = actualProjectDto.getMembers().get(0);
        MemberDto actualSecondMember = actualProjectDto.getMembers().get(1);
        assertEquals(expectedFirstMember
                        .getName(),
                actualFirstMember
                        .getName());
        assertEquals(expectedSecondMember
                        .getName(),
                actualSecondMember
                        .getName());
    }

}
