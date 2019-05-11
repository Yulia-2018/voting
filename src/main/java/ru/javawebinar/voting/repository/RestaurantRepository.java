package ru.javawebinar.voting.repository;

import ru.javawebinar.voting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    // null if not found
    Restaurant get(int id);

    List<Restaurant> getAll();

    List<Restaurant> getAllWithDishes(LocalDate date);
}
