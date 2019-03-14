package ru.javawebinar.voting;

import ru.javawebinar.voting.model.Restaurant;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.voting.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int RESTAURANT2_ID = START_SEQ + 3;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Элис");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Местечко");

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        //assertThat(actual).isEqualToComparingFieldByField(expected);
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "dishes");
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        //assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
        assertThat(actual).usingElementComparatorIgnoringFields("dishes").isEqualTo(expected);
    }
}
