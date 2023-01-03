package com.knotslicer.server.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private Long scheduleId;
    @Column(unique=true, updatable = false, nullable = false)
    private String scheduleBusinessKey;
    private LocalDateTime startTimeUtc;
    private LocalDateTime endTimeUtc;
    private ZoneId memberTimeZone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member member;

    public Schedule() {}
    public Schedule(String scheduleBusinessKey, LocalDateTime startTimeUtc, LocalDateTime endTimeUtc, ZoneId memberTimeZone){
        this.scheduleBusinessKey = scheduleBusinessKey;
        this.startTimeUtc = startTimeUtc;
        this.endTimeUtc = endTimeUtc;
        this.memberTimeZone = memberTimeZone;
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
        Schedule inputSchedule = (Schedule)object;
        return Objects.equals(scheduleBusinessKey, inputSchedule.getScheduleBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(scheduleBusinessKey);
    }

    public String getScheduleBusinessKey() {return scheduleBusinessKey;}
    public void setScheduleBusinessKey(String scheduleBusinessKey) {this.scheduleBusinessKey = scheduleBusinessKey;}
    public LocalDateTime getStartTimeUtc() {return startTimeUtc;}
    public void setStartTimeUtc(LocalDateTime startTimeUtc) {this.startTimeUtc = startTimeUtc;}
    public LocalDateTime getEndTimeUtc() {return endTimeUtc;}
    public void setEndTimeUtc(LocalDateTime endTimeUtc) {this.endTimeUtc = endTimeUtc;}
    public ZoneId getMemberTimeZone() {return memberTimeZone;}
    public void setMemberTimeZone(ZoneId memberTimeZone) {this.memberTimeZone = memberTimeZone;}

    public Member getMember() {return member;}
    public void setMember(Member member) {this.member = member;}

}
