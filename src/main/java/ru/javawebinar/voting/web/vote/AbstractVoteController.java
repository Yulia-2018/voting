package ru.javawebinar.voting.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.service.VoteService;
import ru.javawebinar.voting.to.ResultsVoting;
import ru.javawebinar.voting.web.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.voting.util.ValidationUtil.checkNew;
import static ru.javawebinar.voting.util.VotesUtil.getResultsVoting;

public abstract class AbstractVoteController {
    private static final Logger log = LoggerFactory.getLogger(VoteRestController.class);

    @Autowired
    private VoteService service;

    public Vote create(Vote vote, int restaurantId) {
        int userId = SecurityUtil.authUserId();
        checkNew(vote);
        log.info("user {} create {}", userId, vote);
        return service.create(vote, userId, restaurantId);
    }

    public void update(Vote vote, int id, int restaurantId) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(vote, id);
        log.info("user {} update {}", userId, vote);
        service.update(vote, userId, restaurantId);
    }

    public Vote get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} get vote {}", userId, id);
        return service.get(id, userId);
    }

    public List<ResultsVoting> getResults(LocalDate date) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} getResultsVoting by {}", userId, date);
        List<Vote> votes = service.getAll(date);
        return getResultsVoting(votes, date);
    }
}
