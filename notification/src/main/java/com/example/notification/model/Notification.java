package com.example.notification.model;

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
@ToString
public class Notification {
    private String eventName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm")
    private LocalDateTime startDate;
    private List<String> emails;
}
