package sirkostya009.posterapp.model.dao;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

/**
 * A user model that is stored in database
 */
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
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    private String bio;
    private String profilePictureFilename = null;

    @OneToMany
    @ToString.Exclude
    private List<Poster> posters;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<AppUser> following, followers;

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
        return Collections.emptySet();
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

    /**
     * @deprecated reserved for future use
     */
    public void disable() {
        isEnabled = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser appUser)) return false;
        return username.equals(appUser.username) && password.equals(appUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isExpired, isLocked, isCredentialsExpired, isEnabled, bio);
    }
}
