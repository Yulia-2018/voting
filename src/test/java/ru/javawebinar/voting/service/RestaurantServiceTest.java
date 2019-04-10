package ru.javawebinar.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.voting.RestaurantTestData.*;
import static ru.javawebinar.voting.UserTestData.ADMIN_ID;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class RestaurantServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    void create() {
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
    void update() {
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
    void delete() {
        service.delete(RESTAURANT1_ID, ADMIN_ID);
        assertMatch(service.getAll(), RESTAURANT2);
    }

    // Реализовать такую функциональность
    /*@Test(expected = NoAccessException.class)
    public void deleteNoAccess() {
        service.delete(RESTAURANT1_ID, USER_ID);
    }*/

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(1, ADMIN_ID));
    }

    @Test
    void get() {
        Restaurant restaurant = service.get(RESTAURANT2_ID);
        assertMatch(restaurant, RESTAURANT2);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(1));
    }

    @Test
    void getAll() {
        List<Restaurant> all = service.getAll();
        assertMatch(all, RESTAURANT2, RESTAURANT1);
    }
}