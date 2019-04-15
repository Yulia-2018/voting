package ru.javawebinar.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.javawebinar.voting.RestaurantTestData;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.util.exception.InvalidDateTimeException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT2;
import static ru.javawebinar.voting.UserTestData.ADMIN_ID;
import static ru.javawebinar.voting.UserTestData.USER_ID;
import static ru.javawebinar.voting.VoteTestData.*;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class VoteServiceTest {

    @Autowired
    private VoteService service;

    @Test
    void create() {
        LocalDate date = LocalDate.now();
        Vote newVote = new Vote(null, date, RESTAURANT2);
        Vote created = service.create(newVote, LocalTime.of(10, 25), ADMIN_ID);
        newVote.setId(created.getId());
        RestaurantTestData.assertMatch(newVote.getRestaurant(), created.getRestaurant());
        assertMatch(newVote, created);
        assertMatch(service.getAll(date), newVote);
    }

    @Test
    void createInvalidTime() {
        Vote newVote = new Vote(null, LocalDate.now(), RESTAURANT2);
        assertThrows(InvalidDateTimeException.class, () -> service.create(newVote, LocalTime.of(12, 30), USER_ID));
    }

    @Test
    void createInvalidDate() {
        Vote newVote = new Vote(null, LocalDate.of(2018, 12, 15), RESTAURANT1);
        assertThrows(InvalidDateTimeException.class, () -> service.create(newVote, LocalTime.of(10, 30), USER_ID));
    }

    // Здесь мы сравниваем только id и date, так как restaurant и user мы не сравниваем
    @Test
    void update() {
        Vote created = createForCurrentDate(service);
        Vote updated = new Vote(created);
        updated.setRestaurant(RESTAURANT1);
        service.update(updated, LocalTime.of(9, 35), USER_ID);
        Vote actual = service.get(updated.getId(), USER_ID);
        RestaurantTestData.assertMatch(actual.getRestaurant(), updated.getRestaurant());
        assertMatch(actual, updated);
    }

    @Test
    void updateInvalidTime() {
        //assertThrows(InvalidDateTimeException.class, () -> service.update(VOTE_FOR_CURRENT_DATE, LocalTime.of(15, 25), USER_ID));
        assertThrows(InvalidDateTimeException.class, () -> {
            Vote created = createForCurrentDate(service);
            service.update(created, LocalTime.of(15, 25), USER_ID);
        });
    }

    @Test
    void updateInvalidDate() {
        assertThrows(InvalidDateTimeException.class, () -> service.update(VOTE1_USER, LocalTime.of(10, 0), USER_ID));
    }

    @Test
    void updateNotFound() {
        //assertThrows(NotFoundException.class, () -> service.update(VOTE_FOR_CURRENT_DATE, LocalTime.of(10, 0), ADMIN_ID));
        assertThrows(NotFoundException.class, () -> {
            Vote created = createForCurrentDate(service);
            service.update(created, LocalTime.of(10, 0), ADMIN_ID);
        });
    }

    @Test
    void get() {
        Vote vote = service.get(VOTE1_ID, USER_ID);
        assertMatch(vote, VOTE1_USER);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(VOTE1_ID, ADMIN_ID));
    }

    @Test
    void getAll() {
        assertMatch(service.getAll(LocalDate.of(2019, 2, 1)), VOTE2_ADMIN, VOTE2_USER);
    }
}