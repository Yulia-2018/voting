package ru.javawebinar.voting.repository;

import ru.javawebinar.voting.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository {
    // null if updated dish do not belong to restaurantId
    Dish save(Dish dish, int restaurantId);

    // false if dish do not belong to restaurantId
    boolean delete(int id, int restaurantId);

    // null if dish do not belong to restaurantId
    Dish get(int id, int restaurantId);

    List<Dish> getAll(int restaurantId, LocalDate date);
}
