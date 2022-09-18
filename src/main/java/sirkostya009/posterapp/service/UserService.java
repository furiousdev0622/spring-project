package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sirkostya009.posterapp.model.AppUser;
import sirkostya009.posterapp.repo.UserRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo repo;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsernameOrEmail(username, username) // searches for both username and login, TODO probably needs refactoring
                .orElseThrow(() -> new UsernameNotFoundException("login " + username + " not found"));
    }

    public List<AppUser> findAll() {
        return repo.findAll();
    }

    public AppUser findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("user with id " + id + " not found"));
    }

    public void registerUser(AppUser user) {
        if (repo.findByUsername(user.getUsername()).isPresent())
            throw new RuntimeException("username already taken");

        if (repo.findByEmail(user.getEmail()).isPresent())
            throw new RuntimeException("email already taken");

        user.setPassword(encoder.encode(user.getPassword()));

        repo.save(user);
    }

    public AppUser findByUsername(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("username " + username + " not found"));
    }

    public AppUser findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("email " + email + " not found"));
    }
}
