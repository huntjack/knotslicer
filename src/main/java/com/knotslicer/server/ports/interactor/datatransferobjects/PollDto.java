package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import java.util.List;

@JsonDeserialize(as = PollDtoImpl.class)
public interface PollDto extends Linkable {
    Long getEventId();
    void setEventId(Long eventId);
    Long getPollId();
    void setPollId(Long pollId);
    LocalDateTime getStartTimeUtc();
    void setStartTimeUtc(LocalDateTime startTimeUtc);
    LocalDateTime getEndTimeUtc();
    void setEndTimeUtc(LocalDateTime endTimeUtc);
    List<PollAnswerDto> getPollAnswers();
    void setPollAnswers(List<PollAnswerDto> pollAnswerDtos);
}
