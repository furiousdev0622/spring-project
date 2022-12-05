package sirkostya009.posterapp.email;

/**
 * An interface that sends emails to users made to abstract from different implementations
 */
public interface EmailSender {
    void send(String to, String subject, String body);
}
