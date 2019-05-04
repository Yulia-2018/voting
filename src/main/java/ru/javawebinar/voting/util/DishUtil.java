package ru.javawebinar.voting.util;

import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.to.DishTo;

import java.time.LocalDate;

public class DishUtil {

    public static Dish createNewFromTo(DishTo newDish) {
        return new Dish(null, newDish.getName(), newDish.getPrice(), LocalDate.now());
    }

    public static Dish updateFromTo(Dish dish, DishTo dishTo) {
        dish.setName(dishTo.getName());
        dish.setPrice(dishTo.getPrice());
        return dish;
    }
}
