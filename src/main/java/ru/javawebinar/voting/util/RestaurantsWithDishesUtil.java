package ru.javawebinar.voting.util;

import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.to.RestaurantsWithDishes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.voting.util.DishUtil.getListDistToFromListDish;
import static ru.javawebinar.voting.util.RestaurantUtil.asTo;

public class RestaurantsWithDishesUtil {

    private RestaurantsWithDishesUtil() {
    }

    public static List<RestaurantsWithDishes> getRestaurantsWithDishes(Collection<Restaurant> restaurants, LocalDate date) {
        List<RestaurantsWithDishes> results = new ArrayList<>();
        restaurants.forEach(restaurant -> results.add(new RestaurantsWithDishes(asTo(restaurant), getListDistToFromListDish(restaurant.getDishes()), date)));
        return results;
    }
}
