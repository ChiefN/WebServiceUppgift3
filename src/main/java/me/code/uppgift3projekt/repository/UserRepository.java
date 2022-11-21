package me.code.uppgift3projekt.repository;

import me.code.uppgift3projekt.data.Post;
import me.code.uppgift3projekt.data.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public Optional<User> getByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public Optional<User> save(User user) {
        var existing = users.put(user.getUsername(), user);

        return Optional.ofNullable(existing);
    }

    public Collection<User> getAll() {
        return users.values();
    }

}
