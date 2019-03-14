package ru.javawebinar.voting.repository;

import ru.javawebinar.voting.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {
    // null if the user is not an administrator
    Restaurant save(Restaurant restaurant, int userId);

    // false if the user is not an administrator
    boolean delete(int id, int userId);

    // null if not found
    Restaurant get(int id);

    List<Restaurant> getAll();
}
