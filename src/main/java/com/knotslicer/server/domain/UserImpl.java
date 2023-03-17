package com.knotslicer.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "User")
@Table(name = "User")
public class UserImpl implements User {
    @Id
    @SequenceGenerator(name="user_generator", sequenceName = "user_sequence", allocationSize=1)
    @GeneratedValue(strategy=SEQUENCE, generator="user_generator")
    @Column(updatable = false, nullable = false)
    private Long userId;
    @Column(unique=true, updatable = false, nullable = false)
    private String userBusinessKey;
    @Column(unique=true, nullable = false)
    @Email
    private String email;
    @Column(unique=true, nullable = false)
    @Size(max=50)
    @NotBlank
    private String userName;
    @Size(min=8, max=250)
    @NotBlank
    private String userDescription;
    private ZoneId timeZone;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}, orphanRemoval = true)
    private List<ProjectImpl> projects = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}, orphanRemoval = true)
    private List<MemberImpl> members = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}, orphanRemoval = true)
    private List<EventImpl> events = new ArrayList<>();

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

    public UserImpl() {
        this.userBusinessKey = UUID.randomUUID().toString();
    }

    @Override
    public Long getUserId() {return userId;}
    @Override
    public void setUserId(Long userId) {this.userId = userId;}
    public String getUserBusinessKey() {return userBusinessKey;}
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
    @Override
    public ZoneId getTimeZone() {return timeZone;}
    @Override
    public void setTimeZone(ZoneId timeZone) {this.timeZone = timeZone;}

    public List<ProjectImpl> getProjects() {return projects;}
    public void setProjects(List<ProjectImpl> projects) {this.projects = projects;}
    public List<MemberImpl> getMembers() {return members;}
    public void setMembers(List<MemberImpl> members) {this.members = members;}
    public List<EventImpl> getEvents() {return events;}
    public void setEvents(List<EventImpl> events) {this.events = events;}

}
