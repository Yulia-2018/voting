package ru.javawebinar.voting.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.voting.RestaurantTestData;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.to.ResultVote;
import ru.javawebinar.voting.util.exception.InvalidDateTimeException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT2;
import static ru.javawebinar.voting.UserTestData.ADMIN_ID;
import static ru.javawebinar.voting.UserTestData.USER_ID;
import static ru.javawebinar.voting.VoteTestData.*;
import static ru.javawebinar.voting.web.VoteRestController.RESULT_VOTE_COMPARATOR;

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
        LocalDate date = LocalDate.now();
        Vote newVote = new Vote(null, date, RESTAURANT2);
        Vote created = service.create(newVote, LocalTime.of(10, 25), ADMIN_ID);
        newVote.setId(created.getId());
        RestaurantTestData.assertMatch(newVote.getRestaurant(), created.getRestaurant());
        assertMatch(newVote, created);
        assertMatch(service.getAll(date), VOTE_FOR_CURRENT_DATE, newVote);
    }

    @Test(expected = InvalidDateTimeException.class)
    public void createInvalidTime() {
        Vote newVote = new Vote(null, LocalDate.now(), RESTAURANT2);
        service.create(newVote, LocalTime.of(12, 30), USER_ID);
    }

    @Test(expected = InvalidDateTimeException.class)
    public void createInvalidDate() {
        Vote newVote = new Vote(null, LocalDate.of(2018, 12, 15), RESTAURANT1);
        service.create(newVote, LocalTime.of(10, 30), USER_ID);
    }

    // Здесь мы сравниваем только id и date, так как restaurant и user мы не сравниваем
    @Test
    public void update() {
        Vote updated = new Vote(VOTE_FOR_CURRENT_DATE);
        updated.setRestaurant(RESTAURANT1);
        service.update(updated, LocalTime.of(9, 35), USER_ID);
        Vote actual = service.get(VOTE_ID_FOR_CURRENT_DATE, USER_ID);
        RestaurantTestData.assertMatch(actual.getRestaurant(), updated.getRestaurant());
        assertMatch(actual, updated);
    }

    @Test(expected = InvalidDateTimeException.class)
    public void updateInvalidTime() {
        service.update(VOTE_FOR_CURRENT_DATE, LocalTime.of(15, 25), USER_ID);
    }

    @Test(expected = InvalidDateTimeException.class)
    public void updateInvalidDate() {
        service.update(VOTE1_USER, LocalTime.of(10, 0), USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(VOTE_FOR_CURRENT_DATE, LocalTime.of(10, 0), ADMIN_ID);
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

    // Этот тест для проверки, потом уберу
    @Test
    public void getResult() {
        //LocalDate date = LocalDate.of(2019, 1, 1);
        //LocalDate date = LocalDate.of(2019, 2, 1);
        LocalDate date = LocalDate.now();
        List<Vote> votes = service.getAll(date);
        Map<Restaurant, Long> quantityByRestaurant = votes.stream()
                .collect(
                        Collectors.groupingBy(Vote::getRestaurant, Collectors.counting())
                );
        List<ResultVote> result = new ArrayList<>();
        quantityByRestaurant.forEach((key, value) -> result.add(new ResultVote(key.getId(), key.getName(), date, value.intValue())));
        result.sort(RESULT_VOTE_COMPARATOR);
        System.out.println(result);
    }
}