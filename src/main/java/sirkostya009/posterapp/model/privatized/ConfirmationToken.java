package sirkostya009.posterapp.model.privatized;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Column(nullable = false)
    private LocalDateTime issuedAt;
    private LocalDateTime confirmedAt;

    public ConfirmationToken(String token, Long userId, LocalDateTime expiresAt, LocalDateTime issuedAt) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.issuedAt = issuedAt;
    }
}
