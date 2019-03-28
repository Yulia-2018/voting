package ru.javawebinar.voting.repository;

import ru.javawebinar.voting.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository {
    // null if the user is not an administrator or if updated dish do not belong to restaurantId
    Dish save(Dish dish, int restaurantId, int userId);

    // false if the user is not an administrator or dish do not belong to restaurantId
    boolean delete(int id, int restaurantId, int userId);

    // null if dish do not belong to restaurantId
    Dish get(int id, int restaurantId);

    List<Dish> getAll(int restaurantId, LocalDate date);
}
