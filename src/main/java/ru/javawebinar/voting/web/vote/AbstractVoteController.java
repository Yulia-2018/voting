package ru.javawebinar.voting.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.service.VoteService;
import ru.javawebinar.voting.to.ResultVote;
import ru.javawebinar.voting.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.voting.util.ValidationUtil.checkNew;
import static ru.javawebinar.voting.util.VotesUtil.getFilteredResults;

public abstract class AbstractVoteController {
    private static final Logger log = LoggerFactory.getLogger(VoteRestController.class);

    @Autowired
    private VoteService service;

    public Vote create(Vote vote) {
        // При создании голоса мы скорее всего его будем формировать как то так:
        // Vote vote = new Vote(null, LocalDate.now()), про user и restaurant на т.м. не знаю
        // то есть в метод create голос мы передаем с текущей датой
        int userId = SecurityUtil.authUserId();
        checkNew(vote);
        log.info("user {} create {}", userId, vote);
        return service.create(vote, LocalTime.now(), userId);
    }

    // При редактировании голоса мы скорее всего его будем получать как то так:
    // Vote vote = get(id, userId)
    // то есть в метод update голос мы передаем неизвестно с какой датой, с текущей датой или с любой из предыдущих дат
    public void update(Vote vote, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(vote, id);
        log.info("user {} update {}", userId, vote);
        service.update(vote, LocalTime.now(), userId);
    }

    public Vote get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} get vote {}", userId, id);
        return service.get(id, userId);
    }

    public List<ResultVote> getResult(LocalDate date) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} getResult vote", userId);
        List<Vote> votes = service.getAll(date);
        return getFilteredResults(votes, date);
    }
}
