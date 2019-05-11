package ru.javawebinar.voting.repository;

import ru.javawebinar.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {

    Vote save(Vote vote, int userId, int restaurantId);

    // null if not found or if vote do not belong to userId
    Vote get(int id, int userId);

    List<Vote> getAll(LocalDate date);
}
