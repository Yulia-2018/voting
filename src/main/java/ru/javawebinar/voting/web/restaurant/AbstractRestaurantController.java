package ru.javawebinar.voting.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.service.RestaurantService;
import ru.javawebinar.voting.web.SecurityUtil;

import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.voting.util.ValidationUtil.checkNew;

public abstract class AbstractRestaurantController {
    private static final Logger log = LoggerFactory.getLogger(RestaurantRestController.class);

    @Autowired
    private RestaurantService service;

    public Restaurant create(Restaurant restaurant) {
        int userId = SecurityUtil.authUserId();
        checkNew(restaurant);
        log.info("user {} create {}", userId, restaurant);
        return service.create(restaurant);
    }

    public void update(Restaurant restaurant, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(restaurant, id);
        log.info("user {} update {}", userId, restaurant);
        service.update(restaurant);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} delete restaurant {}", userId, id);
        service.delete(id);
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
