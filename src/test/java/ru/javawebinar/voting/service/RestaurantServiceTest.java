package ru.javawebinar.voting.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.voting.RestaurantTestData.*;
import static ru.javawebinar.voting.UserTestData.ADMIN_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class RestaurantServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    public void create() {
        Restaurant newRestaurant = new Restaurant(null, "Новый ресторан");
        Restaurant created = service.create(newRestaurant, ADMIN_ID);
        newRestaurant.setId(created.getId());
        assertMatch(newRestaurant, created);
        assertMatch(service.getAll(), RESTAURANT2, newRestaurant, RESTAURANT1);
    }

    // Реализовать такую функциональность
    /*@Test(expected = NoAccessException.class)
    public void createNoAccess() {
        Restaurant newRestaurant = new Restaurant(null, "Новый ресторан");
        service.create(newRestaurant, USER_ID);
    }*/

    @Test
    public void update() {
        Restaurant updated = new Restaurant(RESTAURANT1);
        updated.setName("UpdatedName");
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(RESTAURANT1_ID), updated);
    }

    // Реализовать такую функциональность
    /*@Test(expected = NoAccessException.class)
    public void updateNoAccess() {
        Restaurant updated = new Restaurant(RESTAURANT1);
        updated.setName("UpdatedName");
        service.update(updated, USER_ID);
    }*/

    @Test
    public void delete() {
        service.delete(RESTAURANT1_ID, ADMIN_ID);
        assertMatch(service.getAll(), RESTAURANT2);
    }

    // Реализовать такую функциональность
    /*@Test(expected = NoAccessException.class)
    public void deleteNoAccess() {
        service.delete(RESTAURANT1_ID, USER_ID);
    }*/

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(1, ADMIN_ID);
    }

    @Test
    public void get() {
        Restaurant restaurant = service.get(RESTAURANT2_ID);
        assertMatch(restaurant, RESTAURANT2);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(1);
    }

    @Test
    public void getAll() {
        List<Restaurant> all = service.getAll();
        assertMatch(all, RESTAURANT2, RESTAURANT1);
    }
}