package ru.javawebinar.voting.repository;

import ru.javawebinar.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {
    // null if updated vote do not belong to userId
    Vote save(Vote vote, int userId);

    // null if vote do not belong to userId
    Vote get(int id, int userId);

    List<Vote> getAll(LocalDate date);
}