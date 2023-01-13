package com.knotslicer.server.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "Schedule")
@Table(name = "Schedule")
public class ScheduleImpl implements Schedule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private Long scheduleId;
    @Column(unique=true, updatable = false, nullable = false)
    private String scheduleBusinessKey;
    private LocalDateTime startTimeUtc;
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

    public Long getScheduleId() {return scheduleId;}
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
