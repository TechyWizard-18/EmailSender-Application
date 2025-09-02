package com.project.mail.emailsender.controller;
import java.lang.*;
import lombok.*;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Data
public class CustomResponse {

    private String message;
    private HttpStatus status;
    private boolean success=false;
}
