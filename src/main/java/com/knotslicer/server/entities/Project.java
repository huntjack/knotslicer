package com.knotslicer.server.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Project {
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
    private User user;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", orphanRemoval = true)
    private List<Member> members;

    public void addMember(Member member) {
        member.setProject(this);
        members.add(member);
    }
    public void removeMember(Member member) {
        members.remove(member);
        member.setProject(null);
    }
    public Project() {}
    public Project(String projectBusinessKey, String projectName, String projectDescription) {
        this.projectBusinessKey = projectBusinessKey;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
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
        Project inputProject = (Project)object;
        return Objects.equals(projectBusinessKey, inputProject.getProjectBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(projectBusinessKey);
    }

    public String getProjectBusinessKey() {return projectBusinessKey;}
    public void setProjectBusinessKey(String projectBusinessKey) {this.projectBusinessKey = projectBusinessKey;}
    public String getProjectName() {return projectName;}
    public void setProjectName(String projectName) {this.projectName = projectName;}
    public String getProjectDescription() {return projectDescription;}
    public void setProjectDescription(String projectDescription) {this.projectDescription = projectDescription;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public List<Member> getMembers() {return members;}
    public void setMembers(List<Member> members) {this.members = members;}
}
