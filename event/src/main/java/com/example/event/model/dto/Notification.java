package com.example.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString            //using @sl4j in eventService to print this in console
public class Notification {
    private String eventName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm")
    private LocalDateTime startDate;
    private List<String> emails;
}
