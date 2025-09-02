package com.project.mail.emailsender.service;

import com.project.mail.emailsender.model.Message;

import java.io.InputStream;
import java.util.List;

public interface EmailService {

    // Send email (HTML or plain text) to single/multiple recipients
    void sendEmailWithHtml(String[] to, String subject, String htmlContent);

    // Send email with attachment to single/multiple recipients
    void sendEmailWithFile(String[] to, String subject, String message, InputStream inputStream);


    List<Message> getInboxMessages();
}
