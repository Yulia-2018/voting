package ru.javawebinar.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.service.VoteService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.voting.util.ValidationUtil.checkNew;

@Controller
public class VoteRestController {
    private static final Logger log = LoggerFactory.getLogger(VoteRestController.class);

    @Autowired
    private final VoteService service;

    public VoteRestController(VoteService service) {
        this.service = service;
    }

    public Vote create(Vote vote) {
        // При создании голоса, мы скорее всего его будем формировать как то так:
        // Vote vote = new Vote(null, LocalDate.now()), про user и restaurant на т.м. не знаю
        // то есть в метод create голос мы передаем с текущей датой
        int userId = SecurityUtil.authUserId();
        checkNew(vote);
        log.info("user {} create {}", userId, vote);
        LocalTime localTime = LocalTime.now();
        return service.create(vote, localTime, userId);
    }

    // Подумать, как и нужно ли проверить, что пользователь обновляет свой голос текущего дня, а не одного из предыдущих,
    // то есть в vote дата НЕ сегодняшняя
    public void update(Vote vote, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(vote, id);
        log.info("user {} update {}", userId, vote);
        LocalTime localTime = LocalTime.now();
        service.update(vote, localTime, userId);
    }

    public Vote get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} get vote {}", userId, id);
        return service.get(id, userId);
    }

    public List<Vote> getAll(LocalDate date) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} getAll vote", userId);
        return service.getAll(date);
    }
}
