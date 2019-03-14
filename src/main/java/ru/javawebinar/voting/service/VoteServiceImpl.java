package ru.javawebinar.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.repository.VoteRepository;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private final VoteRepository repository;

    public VoteServiceImpl(VoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Vote create(Vote vote, LocalTime localTime, int userId) {
        Assert.notNull(vote, "vote must not be null");
        if (localTime.compareTo(LocalTime.of(11, 0)) < 0) {
            return repository.save(vote, userId);
        }
        return null;
    }

    @Override
    public void update(Vote vote, LocalTime localTime, int userId) throws NotFoundException {
        Assert.notNull(vote, "vote must not be null");
        if (localTime.compareTo(LocalTime.of(11, 0)) < 0) {
            checkNotFoundWithId(repository.save(vote, userId), vote.getId());
        }
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
