package ru.javawebinar.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.service.RestaurantService;

import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.voting.util.ValidationUtil.checkNew;

@Controller
public class RestaurantRestController {
    private static final Logger log = LoggerFactory.getLogger(RestaurantRestController.class);

    @Autowired
    private final RestaurantService service;

    public RestaurantRestController(RestaurantService service) {
        this.service = service;
    }

    public Restaurant create(Restaurant restaurant) {
        int userId = SecurityUtil.authUserId();
        checkNew(restaurant);
        log.info("user {} create {}", userId, restaurant);
        return service.create(restaurant, userId);
    }

    public void update(Restaurant restaurant, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(restaurant, id);
        log.info("user {} update {}", userId, restaurant);
        service.update(restaurant, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} delete restaurant {}", userId, id);
        service.delete(id, userId);
    }

    public Restaurant get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} get restaurant {}", userId, id);
        return service.get(id);
    }

    public List<Restaurant> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("user {} getAll restaurant", userId);
        return service.getAll();
    }
}
