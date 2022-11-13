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
    @OneToOne
    @JoinColumn(nullable = false)
    private AppUser appUser;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Column(nullable = false)
    private LocalDateTime issuedAt;
    private LocalDateTime confirmedAt;

    public ConfirmationToken(String token, AppUser appUser, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.token = token;
        this.appUser = appUser;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }
}
