package com.knotslicer.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Schedule")
@Table(name = "Schedule")
public class ScheduleImpl implements Schedule {
    @Id
    @SequenceGenerator(name="project_generator", sequenceName = "project_sequence", allocationSize=1)
    @GeneratedValue(strategy=SEQUENCE, generator="project_generator")
    @Column(updatable = false, nullable = false)
    private Long scheduleId;
    @Column(unique=true, updatable = false, nullable = false)
    private String scheduleBusinessKey;
    @Future
    private LocalDateTime startTimeUtc;
    @Future
    private LocalDateTime endTimeUtc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private MemberImpl member;

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
        ScheduleImpl inputSchedule = (ScheduleImpl)object;
        return Objects.equals(scheduleBusinessKey, inputSchedule.getScheduleBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(scheduleBusinessKey);
    }
    public ScheduleImpl() {this.scheduleBusinessKey = UUID.randomUUID().toString();}

    @Override
    public Long getScheduleId() {return scheduleId;}
    @Override
    public void setScheduleId(Long scheduleId) {this.scheduleId = scheduleId;}
    public String getScheduleBusinessKey() {return scheduleBusinessKey;}
    @Override
    public LocalDateTime getStartTimeUtc() {return startTimeUtc;}
    @Override
    public void setStartTimeUtc(LocalDateTime startTimeUtc) {this.startTimeUtc = startTimeUtc;}
    @Override
    public LocalDateTime getEndTimeUtc() {return endTimeUtc;}
    @Override
    public void setEndTimeUtc(LocalDateTime endTimeUtc) {this.endTimeUtc = endTimeUtc;}

    public MemberImpl getMember() {return member;}
    public void setMember(MemberImpl member) {this.member = member;}

}
