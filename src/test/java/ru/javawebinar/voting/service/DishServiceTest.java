package ru.javawebinar.voting.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;

import static ru.javawebinar.voting.DishTestData.*;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.voting.RestaurantTestData.RESTAURANT2_ID;
import static ru.javawebinar.voting.UserTestData.ADMIN_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class DishServiceTest {

    @Autowired
    private DishService service;

    @Test
    public void create() {
        Dish newDish = new Dish(null, "Новая еда", 350, LocalDate.of(2019, 3, 15));
        Dish created = service.create(newDish, RESTAURANT1_ID, ADMIN_ID);
        newDish.setId(created.getId());
        assertMatch(newDish, created);
        assertMatch(service.getAll(RESTAURANT1_ID), newDish, DISH1_3, DISH1_4, DISH1_1, DISH1_2);
    }

    // Реализовать такую функциональность
    /*@Test(expected = NoAccessException.class)
    public void createNoAccess() {
        Dish newDish = new Dish(null, "Новая еда", 350, LocalDate.of(2019, 3, 15));
        service.create(newDish, RESTAURANT1_ID, USER_ID);
    }*/

    @Test
    public void update() {
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

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Dish updated = new Dish(DISH2_1);
        updated.setName("UpdatedName");
        service.update(updated, RESTAURANT1_ID, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(DISH1_ID, RESTAURANT1_ID, ADMIN_ID);
        assertMatch(service.getAll(RESTAURANT1_ID), DISH1_3, DISH1_4, DISH1_2);
    }

    // Реализовать такую функциональность
    /*@Test(expected = NoAccessException.class)
    public void deleteNoAccess() {
        service.delete(DISH1_ID, RESTAURANT1_ID, USER_ID);
    }*/

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(DISH1_ID, RESTAURANT2_ID, ADMIN_ID);
    }

    @Test
    public void get() {
        Dish dish = service.get(DISH2_ID, RESTAURANT2_ID);
        assertMatch(dish, DISH2_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(DISH1_ID, RESTAURANT2_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(RESTAURANT1_ID), DISHES1);
    }
}