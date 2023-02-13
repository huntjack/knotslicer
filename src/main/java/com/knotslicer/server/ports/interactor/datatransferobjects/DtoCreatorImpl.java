package com.knotslicer.server.ports.interactor.datatransferobjects;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DtoCreatorImpl implements DtoCreator {
    @Override
    public UserDto createUserDto() {return new UserDtoImpl();}
    @Override
    public UserLightDto createUserLightDto() {return new UserLightDtoImpl();}
    @Override
    public ProjectDto createProjectDto() {
        return new ProjectDtoImpl();
    }
    @Override
    public MemberDto createMemberDto() {
        return new MemberDtoImpl();
    }
    @Override
    public MemberLightDto createMemberLightDto() {return new MemberLightDtoImpl();}
    @Override
    public EventDto createEventDto() {return new EventDtoImpl();}
    @Override
    public ScheduleDto createScheduleDto() {return new ScheduleDtoImpl();}
    @Override
    public PollDto createPollDto() {return new PollDtoImpl();}
    @Override
    public PollAnswerDto createPollAnswerDto() {return new PollAnswerDtoImpl();}
}
