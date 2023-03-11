package com.knotslicer.server.ports.interactor.services;


import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.Schedule;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class MemberServiceImpl implements MemberService {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithTwoParentsDao<Member, User, Project> memberDao;
    private ChildWithOneRequiredParentDao<Schedule, Member> scheduleDao;
    private EventDao eventDao;

    @Override
    public MemberDto create(MemberDto memberDto) {
        Member member = entityDtoMapper
                .toEntity(memberDto);
        Long userId = memberDto.getUserId();
        Long projectId = memberDto.getProjectId();
        member = memberDao.create(
                member,
                userId,
                projectId);
        return entityDtoMapper.toDto(
                member,
                userId,
                projectId);
    }
    @Override
    public MemberDto get(Long memberId) {
        Member member = getMember(memberId);
        Long userId = getUserId(memberId);
        Long projectId = getProjectId(memberId);
        return entityDtoMapper.toDto(
                member,
                userId,
                projectId);
    }
    private Member getMember(Long memberId) {
        Optional<Member> optionalMember = memberDao
                .get(memberId);
        return optionalMember
                .orElseThrow(() -> new EntityNotFoundException());
    }
    private Long getUserId(Long memberId) {
        Optional<User> optionalUser =
                memberDao.getPrimaryParent(memberId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        return user.getUserId();
    }
    private Long getProjectId(Long memberId) {
        Optional<Project> optionalProject = memberDao.getSecondaryParent(memberId);
        Project project = optionalProject
                .orElseThrow(() -> new EntityNotFoundException());
        return project.getProjectId();
    }
    @Override
    public MemberDto getWithChildren(Long memberId) {
        Optional<Member> optionalMemberWithSchedules = scheduleDao.getPrimaryParentWithChildren(memberId);
        Member memberWithSchedules =  optionalMemberWithSchedules
                .orElseThrow(() -> new EntityNotFoundException());
        Long userId = getUserId(memberId);
        Long projectId = getProjectId(memberId);
        MemberDto memberDto =
                entityDtoMapper.toDto(
                        memberWithSchedules,
                        userId,
                        projectId);
        return entityDtoMapper
                .addScheduleDtosToMemberDto(
                        memberDto,
                        memberWithSchedules);
    }
    @Override
    public MemberDto getWithEvents(Long memberId) {
        Optional<Member> optionalMemberWithEvents =
                eventDao.getMemberWithEvents(memberId);
        Member memberWithEvents =  optionalMemberWithEvents
                .orElseThrow(() -> new EntityNotFoundException());
        Long userId = getUserId(memberId);
        Long projectId = getProjectId(memberId);
        MemberDto memberDto = entityDtoMapper
                .toDto(memberWithEvents,
                        userId,
                        projectId);
        return entityDtoMapper
                .addEventDtosToMemberDto(
                        memberDto,
                        memberWithEvents);
    }
    @Override
    public MemberDto update(MemberDto memberDto) {
        Long memberId = memberDto.getMemberId();
        Member memberToBeModified = getMember(memberId);

        memberToBeModified = entityDtoMapper
                .toEntity(memberDto,
                        memberToBeModified);

        Long userId = getUserId(memberId);
        Member updatedMember =
                memberDao.update(
                        memberToBeModified,
                        userId);

        Long projectId = getProjectId(memberId);
        return entityDtoMapper.toDto(
                updatedMember,
                userId,
                projectId);
    }
    @Override
    public void delete(Long memberId) {
        memberDao.delete(memberId);
    }
    @Inject
    public MemberServiceImpl(EntityDtoMapper entityDtoMapper,
                             @ProcessAs(ProcessType.MEMBER)
                             ChildWithTwoParentsDao<Member, User, Project> memberDao,
                             @ProcessAs(ProcessType.SCHEDULE)
                             ChildWithOneRequiredParentDao<Schedule, Member> scheduleDao,
                             EventDao eventDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.memberDao = memberDao;
        this.scheduleDao = scheduleDao;
        this.eventDao = eventDao;
    }
    protected MemberServiceImpl() {}
}
