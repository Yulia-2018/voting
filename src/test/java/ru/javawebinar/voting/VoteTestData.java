package ru.javawebinar.voting;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.voting.model.Vote;
import ru.javawebinar.voting.to.ResultsVoting;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT2;
import static ru.javawebinar.voting.TestUtil.readListFromJsonMvcResult;
import static ru.javawebinar.voting.UserTestData.ADMIN;
import static ru.javawebinar.voting.UserTestData.USER;
import static ru.javawebinar.voting.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {

    public static final int VOTE1_ID = START_SEQ + 13;

    public static final Vote VOTE1_USER = new Vote(VOTE1_ID, LocalDate.of(2019, 1, 1), USER, RESTAURANT1);
    public static final Vote VOTE1_ADMIN = new Vote(VOTE1_ID + 1, LocalDate.of(2019, 1, 1), ADMIN, RESTAURANT1);
    public static final Vote VOTE2_USER = new Vote(VOTE1_ID + 2, LocalDate.of(2019, 2, 1), USER, RESTAURANT1);
    public static final Vote VOTE2_ADMIN = new Vote(VOTE1_ID + 3, LocalDate.of(2019, 2, 1), ADMIN, RESTAURANT2);

    public static final int VOTE_ID_FOR_CURRENT_DATE = VOTE1_ID + 4;
    public static final Vote VOTE_FOR_CURRENT_DATE = new Vote(VOTE_ID_FOR_CURRENT_DATE, LocalDate.now(), USER, RESTAURANT2);

    public static Vote getCreated() {
        return new Vote(null, LocalDate.now(), RESTAURANT1);
    }

    public static Vote getUpdated() {
        return new Vote(VOTE_ID_FOR_CURRENT_DATE, LocalDate.now(), USER, RESTAURANT1);
    }

    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user", "restaurant");
    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user", "restaurant").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Iterable<ResultsVoting> expected) {
        return result -> assertThat(readListFromJsonMvcResult(result, ResultsVoting.class)).isEqualTo(expected);
    }
}
