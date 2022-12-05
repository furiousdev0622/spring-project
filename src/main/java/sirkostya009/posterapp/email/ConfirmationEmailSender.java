package sirkostya009.posterapp.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * An implementation of EmailSender interface that sends ConfirmationTokens to users
 */
@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationEmailSender implements EmailSender {

    private final JavaMailSender sender;

    @Override
    public void send(String to, String subject, String body) {
        var message = sender.createMimeMessage();

        try {
            message.setFrom("noreply@poster.com");
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject(subject);
            message.setContent(body, "text/html");
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new RuntimeException(e);
        }

        sender.send(message);
    }

    public static String generateBody(String token) {
        return "<p>Click <a href=\"http://localhost:8080/api/v1/register/confirm?token="+token+"\">here</a> to confirm yo email</p>";
    }

}
