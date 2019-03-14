package ru.javawebinar.voting.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.javawebinar.voting.RestaurantTestData.*;
import static ru.javawebinar.voting.UserTestData.*;
import static ru.javawebinar.voting.VoteTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class VoteServiceTest {

    @Autowired
    private VoteService service;

    @Test
    public void create() {
        final LocalDate localDate = LocalDate.of(2019, 3, 15);
        // Подумать про это: в newVote указан ADMIN, а в create USER_ID, и это работает
        // Вот так "Vote newVote = new Vote(null, localDate)" тест create не работает,
        // так как мы не передаем restaurantID и соответственно не делаем его reference
        Vote newVote = new Vote(null, localDate, ADMIN, RESTAURANT2);
        Vote created = service.create(newVote, LocalTime.of(10, 25), USER_ID);
        newVote.setId(created.getId());
        assertMatch(newVote, created);
        assertMatch(service.getAll(localDate), newVote);
    }

    // Подумать, какое исключение будет означать "После 11:00 нельзя голосовать", и реализовать такую функциональность
    /*@Test(expected = ...Exception.class)
    public void create...() {
        Vote newVote = new Vote(null, LocalDate.of(2018, 12, 5));
        service.create(newVote, LocalTime.of(12, 30), USER_ID);
    }*/

    // Подумать, как организовать голосование только в текущем дне
    // Смысл этого update, мы здесь сравниваем только id и дату
    // id менять нельзя, date по логике менять тоже нельзя, можно менять только ресторан
    // Что можно проверить в этом тесте, если мы поля ресторан и пользователь не сверяем
    // Подумать, как проверять этот update
    @Test
    public void update() {
        Vote updated = new Vote(VOTE1_USER);
        //updated.setRestaurant(RESTAURANT2);
        service.update(updated, LocalTime.of(9, 35), USER_ID);
        assertMatch(service.get(VOTE1_ID, USER_ID), updated);
    }

    // Сделать проверку на редактирование голоса после 11 часов

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(VOTE1_USER, LocalTime.of(10, 0), ADMIN_ID);
    }

    @Test
    public void get() {
        Vote vote = service.get(VOTE1_ID, USER_ID);
        assertMatch(vote, VOTE1_USER);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(VOTE1_ID, ADMIN_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(LocalDate.of(2019, 2, 1)), VOTE2_ADMIN, VOTE2_USER);
    }
}