package com.project.mail.emailsender.model;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {
    private String[] to;
    private String subject;
    private String body;
}
