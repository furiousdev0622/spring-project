package sirkostya009.posterapp.email;

public interface EmailSender {
    void send(String to, String subject, String body);
}
