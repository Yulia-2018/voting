package ru.javawebinar.voting.service;

import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface VoteService {
    Vote create(Vote vote, LocalTime localTime, int userId);

    void update(Vote vote, LocalTime localTime, int userId) throws NotFoundException;

    Vote get(int id, int userId) throws NotFoundException;

    List<Vote> getAll(LocalDate date);
}
