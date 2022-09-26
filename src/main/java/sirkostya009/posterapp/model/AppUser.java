package sirkostya009.posterapp.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    private String profilePictureFilename = null;

    @OneToMany
    @ToString.Exclude
    private List<Poster> posters;

    private boolean isExpired = false;
    private boolean isLocked = false;
    private boolean isCredentialsExpired = false;
    private boolean isEnabled = false;

    public AppUser(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void enable() {
        isEnabled = true;
    }

    public void disable() {
        isEnabled = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser appUser)) return false;
        return isExpired == appUser.isExpired && isLocked == appUser.isLocked && isCredentialsExpired == appUser.isCredentialsExpired && isEnabled == appUser.isEnabled && id.equals(appUser.id) && email.equals(appUser.email) && username.equals(appUser.username) && password.equals(appUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, username, password, isExpired, isLocked, isCredentialsExpired, isEnabled);
    }
}
