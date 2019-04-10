package ru.javawebinar.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.voting.DishTestData.*;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.voting.UserTestData.ADMIN_ID;

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
        LocalDate date = LocalDate.of(2019, 2, 1);
        Dish newDish = new Dish(null, "Новая еда", 350, date);
        Dish created = service.create(newDish, RESTAURANT1_ID, ADMIN_ID);
        newDish.setId(created.getId());
        assertMatch(newDish, created);
        assertMatch(service.getAll(RESTAURANT1_ID, date), DISH1_3, newDish, DISH1_4);
    }

    // Реализовать такую функциональность
    /*@Test(expected = NoAccessException.class)
    public void createNoAccess() {
        Dish newDish = new Dish(null, "Новая еда", 350, LocalDate.of(2019, 3, 15));
        service.create(newDish, RESTAURANT1_ID, USER_ID);
    }*/

    @Test
    void update() {
        Dish updated = new Dish(DISH2_1);
        updated.setName("UpdatedName");
        service.update(updated, RESTAURANT2_ID, ADMIN_ID);
        assertMatch(service.get(DISH2_ID, RESTAURANT2_ID), updated);
    }

    // Реализовать такую функциональность
    /*@Test(expected = NoAccessException.class)
    public void updateNoAccess() {
        Dish updated = new Dish(DISH2_1);
        updated.setName("UpdatedName");
        service.update(updated, RESTAURANT2_ID, USER_ID);
    }*/

    @Test
    void updateNotFound() {
        Dish updated = new Dish(DISH2_1);
        updated.setName("UpdatedName");
        assertThrows(NotFoundException.class, () -> service.update(updated, RESTAURANT1_ID, ADMIN_ID));
    }

    @Test
    void delete() {
        service.delete(DISH1_ID, RESTAURANT1_ID, ADMIN_ID);
        assertMatch(service.getAll(RESTAURANT1_ID, DISH1_1.getDate()), DISH1_2);
    }

    // Реализовать такую функциональность
    /*@Test(expected = NoAccessException.class)
    public void deleteNoAccess() {
        service.delete(DISH1_ID, RESTAURANT1_ID, USER_ID);
    }*/

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(DISH1_ID, RESTAURANT2_ID, ADMIN_ID));
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