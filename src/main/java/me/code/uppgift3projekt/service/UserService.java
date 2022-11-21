package me.code.uppgift3projekt.service;

import me.code.uppgift3projekt.data.Post;
import me.code.uppgift3projekt.data.User;
import me.code.uppgift3projekt.exception.UserAlreadyExistsException;
import me.code.uppgift3projekt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            UserRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("A user with username '" + username + "' could not be found."));
    }

    public User register(String username, String password)
            throws UserAlreadyExistsException
    {
        var existing = repository.getByUsername(username);
        if (existing.isPresent())
            throw new UserAlreadyExistsException();

        var user = new User(username, passwordEncoder.encode(password));
        repository.save(user);

        return user;
    }

    public Collection<User> getAll() {
        return repository.getAll();
    }
}
