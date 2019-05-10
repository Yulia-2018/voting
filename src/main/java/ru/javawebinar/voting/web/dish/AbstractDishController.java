package ru.javawebinar.voting.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.service.DishService;
import ru.javawebinar.voting.to.DishTo;
import ru.javawebinar.voting.web.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.voting.util.ValidationUtil.checkNew;

public abstract class AbstractDishController {
    private static final Logger log = LoggerFactory.getLogger(DishRestController.class);

    @Autowired
    private DishService service;

    public Dish create(Dish dish, int restaurantId) {
        int userId = SecurityUtil.authUserId();
        checkNew(dish);
        log.info("user {} create {} for restaurant {}", userId, dish, restaurantId);
        return service.create(dish, restaurantId);
    }

    public void update(DishTo dishTo, int id, int restaurantId) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(dishTo, id);
        log.info("user {} update {} for restaurant {}", userId, dishTo, restaurantId);
        service.update(dishTo, restaurantId);
    }

    public void delete(int id, int restaurantId) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} delete dish {} for restaurant {}", userId, id, restaurantId);
        service.delete(id, restaurantId);
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
