package com.knotslicer.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.*;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Member")
@Table(name = "Member")
@NamedQuery(name = "getMemberWithEvents",
        query = "SELECT m FROM Member m " +
                "LEFT JOIN FETCH m.events " +
                "WHERE m.memberId = :memberId")
public class MemberImpl implements Member {
    @Id
    @SequenceGenerator(name="member_generator", sequenceName = "member_sequence", allocationSize=1)
    @GeneratedValue(strategy=SEQUENCE, generator="member_generator")
    @Column(updatable = false, nullable = false)
    private Long memberId;
    @Column(unique=true, updatable = false, nullable = false)
    private String memberBusinessKey;
    @Column(nullable = false)
    @Size(max=50)
    @NotBlank
    private String name;
    @Size(max=100)
    @NotBlank
    private String role;
    @Size(min=8, max=250)
    @NotBlank
    private String roleDescription;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private UserImpl user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="projectId")
    private ProjectImpl project;
    @ManyToMany(mappedBy = "members")
    private Set<EventImpl> events = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}, orphanRemoval = true)
    private List<ScheduleImpl> schedules = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", orphanRemoval = true)
    private List<PollAnswerImpl> pollAnswers = new ArrayList<>();

    public void addEvent(EventImpl event) {
        events.add(event);
        event.getMembers().add(this);
    }
    public void removeEvent(EventImpl event) {
        events.remove(event);
        event.getMembers().remove(this);
    }
    public void addSchedule(ScheduleImpl schedule) {
        schedule.setMember(this);
        schedules.add(schedule);
    }
    public void removeSchedule(ScheduleImpl schedule) {
        schedules.remove(schedule);
        schedule.setMember(null);
    }
    public void addPollAnswer(PollAnswerImpl pollAnswer) {
        pollAnswer.setMember(this);
        pollAnswers.add(pollAnswer);
    }
    public void removePollAnswer(PollAnswerImpl pollAnswer) {
        pollAnswers.remove(pollAnswer);
        pollAnswer.setMember(null);
    }

    @Override
    public boolean equals(Object object) {
        if(this==object){
            return true;
        }
        if(object==null) {
            return false;
        }
        if(getClass() != object.getClass()) {
            return false;
        }
        MemberImpl inputMember = (MemberImpl)object;
        return Objects.equals(memberBusinessKey, inputMember.getMemberBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(memberBusinessKey);
    }
    public MemberImpl() {this.memberBusinessKey = UUID.randomUUID().toString();}

    @Override
    public Long getMemberId() {return memberId;}
    @Override
    public void setMemberId(Long memberId) {this.memberId = memberId;}
    public String getMemberBusinessKey() {return memberBusinessKey;}
    @Override
    public String getName() {return name;}
    @Override
    public void setName(String name) {this.name = name;}
    @Override
    public String getRole() {return role;}
    @Override
    public void setRole(String role) {this.role = role;}
    @Override
    public String getRoleDescription() {return roleDescription;}
    @Override
    public void setRoleDescription(String roleDescription) {this.roleDescription = roleDescription;}

    public UserImpl getUser() {return user;}
    public void setUser(UserImpl user) {this.user = user;}
    public ProjectImpl getProject() {return project;}
    public void setProject(ProjectImpl project) {this.project = project;}
    public Set<EventImpl> getEvents() {return events;}
    public void setEvents(Set<EventImpl> events) {this.events = events;}
    public List<ScheduleImpl> getSchedules() {return schedules;}
    public void setSchedules(List<ScheduleImpl> schedules) {this.schedules = schedules;}
    public List<PollAnswerImpl> getPollAnswers() {return pollAnswers;}
    public void setPollAnswers(List<PollAnswerImpl> pollAnswers) {this.pollAnswers = pollAnswers;}
}
