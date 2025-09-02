package com.project.mail.emailsender.service.impl;

import com.project.mail.emailsender.model.Message;
import com.project.mail.emailsender.service.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Send email (works for single or multiple recipients)
     */
    @Override
    public void sendEmailWithHtml(String[] to, String subject, String htmlContent) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);  // Works for single or multiple recipients
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            logger.info("Email has been sent to: {}", (Object) to);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Send email with attachment (works for single or multiple recipients)
     */
    @Override
    public void sendEmailWithFile(String[] to, String subject, String message, InputStream is) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);

            // Save attachment temporarily
            File file = new File("src/main/resources/samples/attachment.pdf");
            Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(fileSystemResource.getFilename(), file);

            mailSender.send(mimeMessage);
            logger.info("Email with attachment sent to: {}", (Object) to);

        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Failed to send email with file", e);
        }
    }

@Value("${mail.store.protocol}")
    String protocol;

    @Value("${mail.imaps.host}")
    String host;

    @Value("${mail.imaps.port}")
    String port;

    @Value("${spring.mail.username}")
String username;

    @Value("${spring.mail.password}")
    String password;

    @Override
//    public List<Message> getInboxMessages() {
//
//        Properties props = new Properties();
//        props.setProperty("mail.store.protocol", protocol);
//        props.setProperty("mail.imaps.host", host);
//        props.setProperty("mail.imaps.port", port);
//
//        Session session = Session.getDefaultInstance(props);
//
//        try {
//
//            Store  store = session.getStore();
//            store.connect(username, password);
//            Folder inbox = store.getFolder("INBOX");
//inbox.open(Folder.READ_ONLY);
//
//            jakarta.mail.Message[] messages = inbox.getMessages();
//
//            List<Message> subject = new ArrayList<>();
//            for (jakarta.mail.Message message : messages) {
////                System.out.println(message.getSubject());
//
//                String content = getContentFromEmailMessage(message);
//                List<String> files = getFilesFromEmailMessage(message);
////                System.out.println("meow");
//
//
//subject.add(Message.builder().subjects(message.getSubject()).content(content).Files(files).build());
//            }
//            return subject;
//        }
//
//        catch (MessagingException | IOException e){
//
//        }
//
//return null;
//    }

    public List<Message> getInboxMessages() {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", protocol);
        props.setProperty("mail.imaps.host", host);
        props.setProperty("mail.imaps.port", port);

        Session session = Session.getDefaultInstance(props);

        try {
            Store store = session.getStore();
            store.connect(username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // ✅ Get only last 5 messages instead of all
            int messageCount = inbox.getMessageCount();
            int start = Math.max(1, messageCount - 4); // last 5 emails
            jakarta.mail.Message[] messages = inbox.getMessages(start, messageCount);

            List<Message> subject = new ArrayList<>();

            for (jakarta.mail.Message message : messages) {
                String content = getContentFromEmailMessage(message);
                List<String> files = getFilesFromEmailMessage(message);

                subject.add(Message.builder()
                        .subjects(message.getSubject())
                        .content(content)
                        .Files(files)
                        .build());
            }

            return subject;
        } catch (MessagingException | IOException e) {
            e.printStackTrace(); // log exception
        }
        return null;
    }



    private List<String> getFilesFromEmailMessage(jakarta.mail.Message message) throws MessagingException, IOException {
        List<String> files=new ArrayList<>();
        if(message.isMimeType("multipart/*")){
            Multipart content = (Multipart)message.getContent();
            for(int i=0;i<content.getCount(); i++) {


                BodyPart bodyPart = content.getBodyPart(i);
                if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                    InputStream inputStream = bodyPart.getInputStream();
                    File file = new File("src/main/resources/samples/" + bodyPart.getFileName());
//saved the file
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);


                    //urls
                    files.add(file.getAbsolutePath());
                }

            }
        }
        return files;
    }

    private String getContentFromEmailMessage(jakarta.mail.Message message) throws MessagingException, IOException {

        if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
            return (String) message.getContent();
        } else if (message.isMimeType("multipart/*")) {
            Multipart part = (Multipart) message.getContent();
            for (int i = 0; i < part.getCount(); i++) {
                BodyPart bodyPart = part.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    return (String) bodyPart.getContent();
                }
            }
        }
//        System.out.println("heyy");
return "";
    }

}