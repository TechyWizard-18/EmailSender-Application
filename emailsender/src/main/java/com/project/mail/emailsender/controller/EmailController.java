package com.project.mail.emailsender.controller;

import com.project.mail.emailsender.model.EmailRequest;
import com.project.mail.emailsender.model.Message;
import com.project.mail.emailsender.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/email")
@CrossOrigin(origins = "http://localhost:5173")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
        if (emailRequest.getTo() == null || emailRequest.getTo().length == 0) {
            return ResponseEntity.badRequest().body(
                    CustomResponse.builder()
                            .message("No recipients provided")
                            .success(false)
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }

        emailService.sendEmailWithHtml(
                emailRequest.getTo(),
                emailRequest.getSubject(),
                emailRequest.getBody()
        );

        return ResponseEntity.ok(
                CustomResponse.builder()
                        .message("Email sent successfully")
                        .success(true)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PostMapping("/send-with-file")
    public ResponseEntity<?> sendWithFile(@RequestPart EmailRequest request,
                                          @RequestPart("file") MultipartFile file) throws IOException {
        if (request.getTo() == null || request.getTo().length == 0) {
            return ResponseEntity.badRequest().body(
                    CustomResponse.builder()
                            .message("No recipients provided")
                            .success(false)
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }

        emailService.sendEmailWithFile(
                request.getTo(),
                request.getSubject(),
                request.getBody(),
                file.getInputStream()
        );

        return ResponseEntity.ok(
                CustomResponse.builder()
                        .message("Email sent successfully with attachment")
                        .success(true)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/emails")
    public ResponseEntity<List<Message>> getInboxEmails() {
        List<Message> emails = emailService.getInboxMessages(); // assuming your service bean is emailService
        if (emails == null || emails.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(emails);
    }

}
