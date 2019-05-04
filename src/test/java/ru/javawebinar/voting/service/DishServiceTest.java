package ru.javawebinar.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.to.DishTo;
import ru.javawebinar.voting.util.DishUtil;
import ru.javawebinar.voting.util.exception.InvalidDateTimeException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.voting.DishTestData.*;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT2_ID;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class DishServiceTest {

    @Autowired
    private DishService service;

    @Test
    void create() {
        DishTo createdTo = getCreatedTo();
        Dish newDish = DishUtil.createNewFromTo(createdTo);
        Dish created = service.create(newDish, RESTAURANT1_ID);
        newDish.setId(created.getId());
        assertMatch(newDish, created);
        assertMatch(service.getAll(RESTAURANT1_ID, created.getDate()), DISH_FOR_CURRENT_DATE, created);
    }

    @Test
    void update() {
        DishTo updatedTo = getUpdatedTo();
        service.update(updatedTo, RESTAURANT1_ID);
        assertMatch(service.get(DISH_ID_FOR_CURRENT_DATE, RESTAURANT1_ID), DishUtil.updateFromTo(new Dish(DISH_FOR_CURRENT_DATE), updatedTo));
    }

    @Test
    void updateInvalidDate() {
        DishTo updatedTo = new DishTo(DISH1_ID, "Обновленное блюдо", 150);
        assertThrows(InvalidDateTimeException.class, () -> service.update(updatedTo, RESTAURANT1_ID));
    }

    @Test
    void updateNotFound() {
        DishTo updatedTo = getUpdatedTo();
        assertThrows(NotFoundException.class, () -> service.update(updatedTo, RESTAURANT2_ID));
    }

    @Test
    void delete() {
        service.delete(DISH_ID_FOR_CURRENT_DATE, RESTAURANT1_ID);
        assertMatch(service.getAll(RESTAURANT1_ID, DISH_FOR_CURRENT_DATE.getDate()), Collections.emptyList());
    }

    @Test
    void deleteInvalidDate() {
        assertThrows(InvalidDateTimeException.class, () -> service.delete(DISH1_ID, RESTAURANT1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(DISH1_ID, RESTAURANT2_ID));
    }

    @Test
    void get() {
        Dish dish = service.get(DISH2_ID, RESTAURANT2_ID);
        assertMatch(dish, DISH2_1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(DISH1_ID, RESTAURANT2_ID));
    }

    @Test
    void getAll() {
        assertMatch(service.getAll(RESTAURANT1_ID, LocalDate.of(2019, 1, 1)), DISH1_1, DISH1_2);
    }
}