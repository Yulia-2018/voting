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
        Restaurant created = service.create(newRestaurant);
        newRestaurant.setId(created.getId());
        assertMatch(newRestaurant, created);
        assertMatch(service.getAll(), RESTAURANT2, newRestaurant, RESTAURANT1);
    }

    @Test
    void update() {
        Restaurant updated = new Restaurant(RESTAURANT1);
        updated.setName("UpdatedName");
        service.update(updated);
        assertMatch(service.get(RESTAURANT1_ID), updated);
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