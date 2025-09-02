package com.project.mail.emailsender;

import com.project.mail.emailsender.model.Message;
import com.project.mail.emailsender.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    private EmailService emailService;

    // 🔹 1. Real email sending test
//    @Test
//    void realEmailSendTest() {
//        emailService.sendEmail("itskartik098@gmail.com", "Nothing Time Pass", "Message from real test");
//        System.out.println("✅ Real email sent successfully!");
//    }
//
//    @Test
//    void testSendEmailWithMultipleRecipients() {
//        // Arrange
//        String[] to = {
//                "itskartik098@gmail.com",
//                "build.inmobi@gmail.com"
//        };
//        String subject = "Test Subject";
//        String message = "This is a test email.";
//
//        // Act
//        emailService.sendEmail(to, subject, message);
//
//        System.out.println("message send Sucessfully!");
//    }
//
//
//    @Test
//    void htmlEmailSendTest() {
//        // Arrange
//
//        String htmlContent = """
//                <html>
//                  <body>
//                    <h2 style="color:blue;">Hello from Spring Boot!</h2>
//                    <p>This is a <b>test HTML email</b> sent using <i>Spring Mail</i>.</p>
//                    <br/>
//                    <a href="https://spring.io/projects/spring-boot">Visit Spring Boot</a>
//                  </body>
//                </html>
//                """;
//
//        emailService.sendEmailWithHtml(
//                "itskartik098@gmail.com",
//                "HTML Email Test",
//                htmlContent
//        );
//
//        System.out.println("✅ HTML Email sent successfully!");
//    }
//@Test
//    void testEmailSendWithAttachment() {
//
//        emailService.sendEmailWithFile( "itskartik098@gmail.com", "Nothing Time Pass", "Message from real test",new File("D:\\Spring Projects\\emailsender\\src\\main\\resources\\static\\LOGO.png"));
//
//        System.out.println("message send Sucessfully!");
//    }
//
//@Test
//    void sendEmailWithFileWithStream() {
//        File file = new File( "D:\\Spring Projects\\emailsender\\src\\main\\resources\\Kartik_Declaration.pdf");
//        try {
//            InputStream is = new FileInputStream(file);
//            emailService.sendEmailWithFile( "itskartik098@gmail.com",
//                     "Email with file",
//                     "This email contains file", is
//
//);
//
//        } catch (FileNotFoundException e) {
//
//
//            throw new RuntimeException(e);
//
//        }
//    }

@Test
    void inboxTest() {
    List<Message> inboxMessages = emailService.getInboxMessages();

    inboxMessages.forEach(item -> {
        System.out.println(item.getSubjects());
        System.out.println(item.getContent());
        System.out.println(item.getFiles());
        System.out.println("-------------");

    });
}
}

