package ru.javawebinar.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.service.DishService;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.voting.util.ValidationUtil.checkNew;

@Controller
public class DishRestController {
    private static final Logger log = LoggerFactory.getLogger(DishRestController.class);

    @Autowired
    private final DishService service;

    public DishRestController(DishService service) {
        this.service = service;
    }

    public Dish create(Dish dish, int restaurantId) {
        int userId = SecurityUtil.authUserId();
        checkNew(dish);
        log.info("user {} create {} for restaurant {}", userId, dish, restaurantId);
        return service.create(dish, restaurantId, userId);
    }

    public void update(Dish dish, int id, int restaurantId) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(dish, id);
        log.info("user {} update {} for restaurant {}", userId, dish, restaurantId);
        service.update(dish, restaurantId, userId);
    }

    public void delete(int id, int restaurantId) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} delete dish {} for restaurant {}", userId, id, restaurantId);
        service.delete(id, restaurantId, userId);
    }

    public Dish get(int id, int restaurantId) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} get dish {} for restaurant {}", userId, id, restaurantId);
        return service.get(id, restaurantId);
    }

    public List<Dish> getAll(int restaurantId, LocalDate date) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} getAll dish for restaurant {} for {}", userId, restaurantId, date);
        return service.getAll(restaurantId, date);
    }
}
