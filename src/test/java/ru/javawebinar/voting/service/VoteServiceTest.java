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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1_ID;
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
        Vote newVote = getCreated();
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) <= 0) {
            Vote created = service.create(newVote, ADMIN_ID, RESTAURANT1_ID);
            newVote.setId(created.getId());
            RestaurantTestData.assertMatch(newVote.getRestaurant(), created.getRestaurant());
            assertMatch(newVote, created);
            assertMatch(service.getAll(newVote.getDate()), VOTE_FOR_CURRENT_DATE, newVote);
        } else {
            assertThrows(InvalidDateTimeException.class, () -> service.create(newVote, USER_ID, RESTAURANT1_ID));
        }
    }

    @Test
    void createInvalidDate() {
        Vote newVote = new Vote(null, LocalDate.of(2018, 12, 15));
        assertThrows(InvalidDateTimeException.class, () -> service.create(newVote, USER_ID, RESTAURANT1_ID));
    }

    @Test
    void update() {
        Vote updated = getUpdated();
        if (LocalTime.now().compareTo(LocalTime.of(11, 0)) <= 0) {
            service.update(updated, USER_ID, RESTAURANT1_ID);
            Vote actual = service.get(VOTE_ID_FOR_CURRENT_DATE, USER_ID);
            RestaurantTestData.assertMatch(actual.getRestaurant(), updated.getRestaurant());
            assertMatch(actual, updated);
        } else {
            assertThrows(InvalidDateTimeException.class, () -> service.update(updated, USER_ID, RESTAURANT1_ID));
        }
    }

    @Test
    void updateInvalidDate() {
        assertThrows(InvalidDateTimeException.class, () -> service.update(VOTE1_USER, USER_ID, RESTAURANT1_ID));
    }

    @Test
    void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID, RESTAURANT1_ID));
    }

    @Test
    void updateDate() {
        Vote updated = new Vote(VOTE1_USER);
        LocalDate newDate = LocalDate.of(2019, 5, 3);
        updated.setDate(newDate);
        InvalidDateTimeException e = assertThrows(InvalidDateTimeException.class, () -> service.update(updated, USER_ID, RESTAURANT1_ID));
        String msg = e.getMessage();
        assertTrue(msg.contains("The date of voting " + VOTE1_USER.getDate() + " cannot be changed to date " + newDate));
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
}