package sirkostya009.posterapp.model.dao;

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
    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser appUser;
    private String email;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Column(nullable = false)
    private LocalDateTime issuedAt;
    private LocalDateTime confirmedAt;

    public ConfirmationToken(String token, AppUser appUser, String email, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.token = token;
        this.appUser = appUser;
        this.email = email;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }
}
