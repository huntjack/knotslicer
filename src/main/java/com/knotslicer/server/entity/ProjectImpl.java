package com.knotslicer.server.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "Project")
@Table(name = "Project")
public class ProjectImpl implements Project {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private Long projectId;
    @Column(unique=true, updatable = false, nullable = false)
    private String projectBusinessKey;
    @Column(unique=true, nullable = false)
    private String projectName;
    private String projectDescription;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private UserImpl user;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", orphanRemoval = true)
    private List<MemberImpl> members;

    public void addMember(MemberImpl member) {
        member.setProject(this);
        members.add(member);
    }
    public void removeMember(MemberImpl member) {
        members.remove(member);
        member.setProject(null);
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
        ProjectImpl inputProject = (ProjectImpl)object;
        return Objects.equals(projectBusinessKey, inputProject.getProjectBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(projectBusinessKey);
    }
    public ProjectImpl() {this.projectBusinessKey = UUID.randomUUID().toString();}

    public Long getProjectId() {return projectId;}
    public String getProjectBusinessKey() {return projectBusinessKey;}
    @Override
    public String getProjectName() {return projectName;}
    @Override
    public void setProjectName(String projectName) {this.projectName = projectName;}
    @Override
    public String getProjectDescription() {return projectDescription;}
    @Override
    public void setProjectDescription(String projectDescription) {this.projectDescription = projectDescription;}
    public UserImpl getUser() {return user;}
    public void setUser(UserImpl user) {this.user = user;}
    public List<MemberImpl> getMembers() {return members;}
    public void setMembers(List<MemberImpl> members) {this.members = members;}
}
