package ru.javawebinar.voting;

import ru.javawebinar.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT2;
import static ru.javawebinar.voting.UserTestData.ADMIN;
import static ru.javawebinar.voting.UserTestData.USER;
import static ru.javawebinar.voting.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {

    public static final int VOTE1_ID = START_SEQ + 12;

    public static final Vote VOTE1_USER = new Vote(VOTE1_ID, LocalDate.of(2019, 1, 1), USER, RESTAURANT1);
    public static final Vote VOTE1_ADMIN = new Vote(VOTE1_ID + 1, LocalDate.of(2019, 1, 1), ADMIN, RESTAURANT1);
    public static final Vote VOTE2_USER = new Vote(VOTE1_ID + 2, LocalDate.of(2019, 2, 1), USER, RESTAURANT1);
    public static final Vote VOTE2_ADMIN = new Vote(VOTE1_ID + 3, LocalDate.of(2019, 2, 1), ADMIN, RESTAURANT2);

    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user", "restaurant");
    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user", "restaurant").isEqualTo(expected);
    }
}
