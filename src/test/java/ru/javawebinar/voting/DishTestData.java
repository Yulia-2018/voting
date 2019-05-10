package ru.javawebinar.voting;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.to.DishTo;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT2;
import static ru.javawebinar.voting.TestUtil.readListFromJsonMvcResult;
import static ru.javawebinar.voting.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static final int DISH1_ID = START_SEQ + 4;
    public static final int DISH2_ID = START_SEQ + 8;

    public static final Dish DISH1_1 = new Dish(DISH1_ID, "Картошечка", 100, LocalDate.of(2019, 1, 1), RESTAURANT1);
    public static final Dish DISH1_2 = new Dish(DISH1_ID + 1, "Шашлычок", 250, LocalDate.of(2019, 1, 1), RESTAURANT1);
    public static final Dish DISH1_3 = new Dish(DISH1_ID + 2, "Компот", 50, LocalDate.of(2019, 2, 1), RESTAURANT1);
    public static final Dish DISH1_4 = new Dish(DISH1_ID + 3, "Цезарь", 230, LocalDate.of(2019, 2, 1), RESTAURANT1);

    public static final Dish DISH2_1 = new Dish(DISH2_ID, "Суп харчо", 200, LocalDate.of(2019, 1, 1), RESTAURANT2);
    public static final Dish DISH2_2 = new Dish(DISH2_ID + 1,  "Тирамису", 180, LocalDate.of(2019, 1, 1), RESTAURANT2);
    public static final Dish DISH2_3 = new Dish(DISH2_ID + 2, "Мороженое", 150, LocalDate.of(2019, 2, 1), RESTAURANT2);
    public static final Dish DISH2_4 = new Dish(DISH2_ID + 3, "Коктейль", 280, LocalDate.of(2019, 2, 1), RESTAURANT2);

    public static final List<Dish> DISHES1 = List.of(DISH1_3, DISH1_4, DISH1_1, DISH1_2);
    public static final List<Dish> DISHES2 = List.of(DISH2_4, DISH2_3, DISH2_1, DISH2_2);

    public static final int DISH_ID_FOR_CURRENT_DATE = START_SEQ + 12;
    public static final Dish DISH_FOR_CURRENT_DATE = new Dish(DISH_ID_FOR_CURRENT_DATE, "Минтай", 380, LocalDate.now(), RESTAURANT1);

    public static DishTo getCreatedTo() {
        return new DishTo(null, "Новое блюдо", 380);
    }

    public static DishTo getUpdatedTo() {
        return new DishTo(DISH_ID_FOR_CURRENT_DATE, "Обновленное блюдо", 150);
    }

    public static void assertMatch(Dish actual, Dish expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "restaurant");
    }

    public static void assertMatch(Iterable<Dish> actual, Dish... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Dish> actual, Iterable<Dish> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Dish... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Dish.class), List.of(expected));
    }
}
