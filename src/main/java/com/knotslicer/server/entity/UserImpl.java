package com.knotslicer.server.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class UserImpl implements User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private Long userId;
    @Column(unique=true, updatable = false, nullable = false)
    private String userBusinessKey;
    @Column(unique=true, nullable = false)
    private String email;
    @Column(unique=true, nullable = false)
    private String userName;
    private String userDescription;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<ProjectImpl> projects;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<MemberImpl> members;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<EventImpl> events;

    public void addProject(ProjectImpl project) {
        project.setUser(this);
        projects.add(project);
    }
    public void removeProject(ProjectImpl project) {
        projects.remove(project);
        project.setUser(null);
    }
    public void addMember(MemberImpl member) {
        member.setUser(this);
        members.add(member);
    }
    public void removeMember(MemberImpl member) {
        members.remove(member);
        member.setUser(null);
    }
    public void addEvent(EventImpl event) {
        event.setUser(this);
        events.add(event);
    }
    public void removeEvent(EventImpl event) {
        events.remove(event);
        event.setUser(null);
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
        UserImpl inputUser = (UserImpl)object;
        return Objects.equals(userBusinessKey, inputUser.getUserBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(userBusinessKey);
    }

    public UserImpl(String userBusinessKey) {
        this.userBusinessKey = userBusinessKey;
    }
    public UserImpl() {}

    public String getUserBusinessKey() {return userBusinessKey;}
    public void setUserBusinessKey(String userBusinessKey) {this.userBusinessKey = userBusinessKey;}
    @Override
    public String getEmail() {return email;}
    @Override
    public void setEmail(String email) {this.email = email;}
    @Override
    public String getUserName() {return userName;}
    @Override
    public void setUserName(String userName) {this.userName = userName;}
    @Override
    public String getUserDescription() {return userDescription;}
    @Override
    public void setUserDescription(String userDescription) {this.userDescription = userDescription;}

    public List<ProjectImpl> getProjects() {return projects;}
    public void setProjects(List<ProjectImpl> projects) {this.projects = projects;}
    public List<MemberImpl> getMembers() {return members;}
    public void setMembers(List<MemberImpl> members) {this.members = members;}
    public List<EventImpl> getEvents() {return events;}
    public void setEvents(List<EventImpl> events) {this.events = events;}

}
