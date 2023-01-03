package com.knotslicer.server.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private Long memberId;
    @Column(unique=true, updatable = false, nullable = false)
    private String memberBusinessKey;
    @Column(unique=true, nullable = false)
    private String name;
    private String role;
    private String roleDescription;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="projectId")
    private Project project;
    @ManyToMany(mappedBy = "members")
    private Set<Event> events;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", orphanRemoval = true)
    private List<Schedule> schedules;
    public void addSchedule(Schedule schedule) {
        schedule.setMember(this);
        schedules.add(schedule);
    }
    public void removeSchedule(Schedule schedule) {
        schedules.remove(schedule);
        schedule.setMember(null);
    }

    public Member(){}
    public Member(String memberBusinessKey, String name, String role, String roleDescription){
        this.memberBusinessKey = memberBusinessKey;
        this.name = name;
        this.role = role;
        this.roleDescription = role;
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
        Member inputMember = (Member)object;
        return Objects.equals(memberBusinessKey, inputMember.getMemberBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(memberBusinessKey);
    }

    public String getMemberBusinessKey() {return memberBusinessKey;}
    public void setMemberBusinessKey(String memberBusinessKey) {this.memberBusinessKey = memberBusinessKey;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
    public String getRoleDescription() {return roleDescription;}
    public void setRoleDescription(String roleDescription) {this.roleDescription = roleDescription;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public Project getProject() {return project;}
    public void setProject(Project project) {this.project = project;}
    public Set<Event> getEvents() {return events;}
    public void setEvents(Set<Event> events) {this.events = events;}
    public List<Schedule> getSchedules() {return schedules;}
    public void setSchedules(List<Schedule> schedules) {this.schedules = schedules;}
}
