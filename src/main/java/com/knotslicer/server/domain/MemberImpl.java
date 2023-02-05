package com.knotslicer.server.domain;

import jakarta.persistence.*;

import java.util.*;

@Entity(name = "Member")
@Table(name = "Member")
public class MemberImpl implements Member {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long memberId;
    @Column(unique=true, updatable = false, nullable = false)
    private String memberBusinessKey;
    @Column(nullable = false)
    private String name;
    private String role;
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
}
