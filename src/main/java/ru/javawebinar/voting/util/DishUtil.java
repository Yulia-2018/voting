package ru.javawebinar.voting.util;

import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.to.DishTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DishUtil {

    private DishUtil() {
    }

    public static Dish createNewFromTo(DishTo newDish) {
        return new Dish(null, newDish.getName(), newDish.getPrice(), LocalDate.now());
    }

    public static Dish updateFromTo(Dish dish, DishTo dishTo) {
        dish.setName(dishTo.getName());
        dish.setPrice(dishTo.getPrice());
        return dish;
    }

    public static DishTo asTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice());
    }

    public static List<DishTo> getListDistToFromListDish(List<Dish> dishList) {
        List<DishTo> dishToList = new ArrayList<>();
        dishList.forEach(dish -> dishToList.add(asTo(dish)));
        return dishToList;
    }
}
