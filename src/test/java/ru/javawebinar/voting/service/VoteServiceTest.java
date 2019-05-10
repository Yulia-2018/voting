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

import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.voting.UserTestData.ADMIN_ID;
import static ru.javawebinar.voting.UserTestData.USER_ID;
import static ru.javawebinar.voting.VoteTestData.*;
import static ru.javawebinar.voting.util.ValidationUtil.TIME;

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
        if (LocalTime.now().compareTo(TIME) <= 0) {
            Vote created = service.create(newVote, ADMIN_ID, RESTAURANT1_ID);
            newVote.setId(created.getId());
            RestaurantTestData.assertMatch(newVote.getRestaurant(), created.getRestaurant());
            assertMatch(newVote, created);
            assertMatch(service.getAll(newVote.getDate()), VOTE_FOR_CURRENT_DATE, newVote);
        } else {
            InvalidDateTimeException e = assertThrows(InvalidDateTimeException.class, () -> service.create(newVote, ADMIN_ID, RESTAURANT1_ID));
            String msg = e.getMessage();
            checkContentsString(msg);
        }
    }

    @Test
    void createInvalidDate() {
        LocalDate date = LocalDate.of(2018, 12, 15);
        Vote newVote = new Vote(null, date);
        InvalidDateTimeException e = assertThrows(InvalidDateTimeException.class, () -> service.create(newVote, USER_ID, RESTAURANT1_ID));
        String msg = e.getMessage();
        if (LocalTime.now().compareTo(TIME) <= 0) {
            assertEquals(msg, "Date " + date + " is invalid");
        } else {
            checkContentsString(msg);
        }
    }

    @Test
    void update() {
        Vote updated = getUpdated();
        if (LocalTime.now().compareTo(TIME) <= 0) {
            service.update(updated, USER_ID, RESTAURANT1_ID);
            Vote actual = service.get(VOTE_ID_FOR_CURRENT_DATE, USER_ID);
            RestaurantTestData.assertMatch(actual.getRestaurant(), updated.getRestaurant());
            assertMatch(actual, updated);
        } else {
            InvalidDateTimeException e = assertThrows(InvalidDateTimeException.class, () -> service.update(updated, USER_ID, RESTAURANT1_ID));
            String msg = e.getMessage();
            checkContentsString(msg);
        }
    }

    @Test
    void updateInvalidDate() {
        InvalidDateTimeException e = assertThrows(InvalidDateTimeException.class, () -> service.update(VOTE1_USER, USER_ID, RESTAURANT1_ID));
        String msg = e.getMessage();
        if (LocalTime.now().compareTo(TIME) <= 0) {
            assertEquals(msg, "Date " + VOTE1_USER.getDate() + " is invalid");
        } else {
            checkContentsString(msg);
        }
    }

    @Test
    void updateNotFound() {
        if (LocalTime.now().compareTo(TIME) <= 0) {
            NotFoundException e = assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID, RESTAURANT1_ID));
            String msg = e.getMessage();
            assertEquals(msg, "Not found entity with id=" + VOTE_ID_FOR_CURRENT_DATE);
        } else {
            InvalidDateTimeException e = assertThrows(InvalidDateTimeException.class, () -> service.update(getUpdated(), ADMIN_ID, RESTAURANT1_ID));
            String msg = e.getMessage();
            checkContentsString(msg);
        }
    }

    @Test
    void updateDate() {
        Vote updated = new Vote(VOTE1_USER);
        LocalDate newDate = LocalDate.now();
        updated.setDate(newDate);
        InvalidDateTimeException e = assertThrows(InvalidDateTimeException.class, () -> service.update(updated, USER_ID, RESTAURANT1_ID));
        String msg = e.getMessage();
        if (LocalTime.now().compareTo(TIME) <= 0) {
            assertEquals(msg, "Date " + VOTE1_USER.getDate() + " is invalid");
        } else {
            checkContentsString(msg);
        }
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

    private void checkContentsString(String msg) {
        assertTrue(msg.contains("Time"));
        assertTrue(msg.contains("is invalid"));
    }
}