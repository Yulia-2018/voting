package ru.javawebinar.voting.repository;

import ru.javawebinar.voting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    // false if not found
    boolean delete(int id);

    // null if not found
    Restaurant get(int id);

    List<Restaurant> getAll();

    default List<Restaurant> getAllWithDishes(LocalDate date){
        throw new UnsupportedOperationException();
    }
}
