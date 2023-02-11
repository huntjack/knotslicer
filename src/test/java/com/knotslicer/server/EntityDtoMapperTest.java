package com.knotslicer.server;

import com.knotslicer.server.domain.MemberImpl;
import com.knotslicer.server.domain.ProjectImpl;
import com.knotslicer.server.domain.UserImpl;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.EntityCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapperImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityDtoMapperTest {
    EntityCreator entityCreator = new EntityCreatorImpl();
    DtoCreator dtoCreator = new DtoCreatorImpl();
    EntityDtoMapper entityDtoMapper = new EntityDtoMapperImpl(entityCreator, dtoCreator);
    @Test
    void testAddMembers() {
        ProjectImpl projectImpl = (ProjectImpl) entityCreator.createProject();
        MemberImpl expectedFirstMember = (MemberImpl) entityCreator.createMember();
        expectedFirstMember.setName("John");
        UserImpl firstMembersUser = (UserImpl) entityCreator.createUser();
        firstMembersUser.addMember(expectedFirstMember);
        MemberImpl expectedSecondMember = (MemberImpl) entityCreator.createMember();
        expectedSecondMember.setName("Sally");
        UserImpl secondMembersUser = (UserImpl) entityCreator.createUser();
        secondMembersUser.addMember(expectedSecondMember);
        projectImpl.addMember(expectedFirstMember);
        projectImpl.addMember(expectedSecondMember);
        ProjectDto actualProjectDto = dtoCreator.createProjectDto();
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
