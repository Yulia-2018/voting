package ru.javawebinar.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.repository.VoteRepository;
import ru.javawebinar.voting.util.exception.InvalidDateTimeException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.*;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private final VoteRepository repository;

    public VoteServiceImpl(VoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Vote create(Vote vote, int userId, int restaurantId) throws InvalidDateTimeException {
        Assert.notNull(vote, "vote must not be null");
        if (vote.getDate() == null) {
            vote.setDate(LocalDate.now());
        }
        checkInvalidDateTime(vote.getDate(), LocalTime.now());
        return repository.save(vote, userId, restaurantId);
    }

    @Transactional
    @Override
    public void update(Vote vote, int userId, int restaurantId) throws NotFoundException, InvalidDateTimeException {
        Assert.notNull(vote, "vote must not be null");
        if (vote.getDate() == null) {
            vote.setDate(LocalDate.now());
        }
        int id = vote.getId();
        LocalDate date = vote.getDate();
        Vote voteInDb = get(id, userId);
        checkInvalidDate(date, voteInDb.getDate());
        checkInvalidDateTime(date, LocalTime.now());
        repository.save(vote, userId, restaurantId);
    }

    @Override
    public Vote get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public List<Vote> getAll(LocalDate date) {
        return repository.getAll(date);
    }
}
