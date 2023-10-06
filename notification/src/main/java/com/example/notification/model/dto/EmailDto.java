package com.example.notification.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailDto {
    private String to;
    private String title;
    private String content;
}
