package ru.javawebinar.voting.service;

import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.util.exception.InvalidDateTimeException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface VoteService {
    Vote create(Vote vote, LocalTime time, int userId, int restaurantId) throws InvalidDateTimeException;

    void update(Vote vote, LocalTime time, int userId, int restaurantId) throws NotFoundException, InvalidDateTimeException;

    Vote get(int id, int userId) throws NotFoundException;

    List<Vote> getAll(LocalDate date);
}
