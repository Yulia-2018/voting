package ru.javawebinar.voting.repository;

import ru.javawebinar.voting.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    // null if not found
    User get(int id);

    // null if not found
    User getByEmail(String email);

    List<User> getAll();
}
