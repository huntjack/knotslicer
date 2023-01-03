package com.knotslicer.server.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class User {
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
    private List<Project> projects;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Member> members;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Event> events;

    public void addProject(Project project) {
        project.setUser(this);
        projects.add(project);
    }
    public void removeProject(Project project) {
        projects.remove(project);
        project.setUser(null);
    }
    public void addMember(Member member) {
        member.setUser(this);
        members.add(member);
    }
    public void removeMember(Member member) {
        members.remove(member);
        member.setUser(null);
    }
    public void addEvent(Event event) {
        event.setUser(this);
        events.add(event);
    }
    public void removeEvent(Event event) {
        events.remove(event);
        event.setUser(null);
    }
    public User() {}
    public User(String userBusinessKey, String email, String userName, String userDescription) {
        this.userBusinessKey = userBusinessKey;
        this.email = email;
        this.userName = userName;
        this.userDescription = userDescription;
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
        User inputUser = (User)object;
        return Objects.equals(userBusinessKey, inputUser.getUserBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(userBusinessKey);
    }

    public String getUserBusinessKey() {return userBusinessKey;}
    public void setUserBusinessKey(String userBusinessKey) {this.userBusinessKey = userBusinessKey;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}
    public String getUserDescription() {return userDescription;}
    public void setUserDescription(String userDescription) {this.userDescription = userDescription;}

    public List<Project> getProjects() {return projects;}
    public void setProjects(List<Project> projects) {this.projects = projects;}
    public List<Member> getMembers() {return members;}
    public void setMembers(List<Member> members) {this.members = members;}
    public List<Event> getEvents() {return events;}
    public void setEvents(List<Event> events) {this.events = events;}

}
