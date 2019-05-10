package ru.javawebinar.voting.service;

import ru.javawebinar.voting.model.User;
import ru.javawebinar.voting.to.UserTo;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.util.List;

public interface UserService {

    User create(User user);

    void update(User user);

    void update(UserTo userTo);

    User get(int id) throws NotFoundException;

    User getByEmail(String email) throws NotFoundException;

    List<User> getAll();

    void enable(int id, boolean enable);
}
