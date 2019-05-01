package ru.javawebinar.voting.service;

import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.util.List;

public interface RestaurantService {

    Restaurant create(Restaurant restaurant);

    void update(Restaurant restaurant);

    void delete(int id) throws NotFoundException;

    Restaurant get(int id) throws NotFoundException;

    List<Restaurant> getAll();
}