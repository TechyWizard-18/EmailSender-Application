package com.project.mail.emailsender.model;

import lombok.*;

import java.util.List;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String from;
    private String content;
    private List<String> Files;
    private String subjects;
}
