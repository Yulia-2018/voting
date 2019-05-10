package ru.javawebinar.voting;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.to.RestaurantTo;
import ru.javawebinar.voting.to.RestaurantsWithDishes;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.voting.DishTestData.DISHES1;
import static ru.javawebinar.voting.DishTestData.DISHES2;
import static ru.javawebinar.voting.TestUtil.readListFromJsonMvcResult;
import static ru.javawebinar.voting.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final int RESTAURANT1_ID = START_SEQ + 2;
    public static final int RESTAURANT2_ID = START_SEQ + 3;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Элис", DISHES1);
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Местечко", DISHES2);

    public static RestaurantTo getCreatedTo() {
        return new RestaurantTo(null, "Новый ресторан");
    }

    public static RestaurantTo getUpdatedTo() {
        return new RestaurantTo(RESTAURANT1_ID, "Обновленный ресторан");
    }

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

    public static ResultMatcher contentJson(Restaurant... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Restaurant.class), List.of(expected));
    }

    public static ResultMatcher contentJson(Iterable<RestaurantsWithDishes> expected) {
        return result -> assertThat(readListFromJsonMvcResult(result, RestaurantsWithDishes.class)).isEqualTo(expected);
    }
}
