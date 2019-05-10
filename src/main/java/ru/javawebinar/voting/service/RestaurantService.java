package ru.javawebinar.voting.service;

import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.to.RestaurantTo;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantService {

    Restaurant create(Restaurant restaurant);

    void update(RestaurantTo restaurantTo);

    Restaurant get(int id) throws NotFoundException;

    List<Restaurant> getAll();

    List<Restaurant> getAllWithDishes(LocalDate date);
}