package ru.javawebinar.voting.service;

import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.util.exception.NoAccessException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.util.List;

public interface RestaurantService {

    Restaurant create(Restaurant restaurant, int userId) throws NoAccessException;

    void update(Restaurant restaurant, int userId) throws NoAccessException;

    void delete(int id, int userId) throws NotFoundException, NoAccessException;

    Restaurant get(int id) throws NotFoundException;

    List<Restaurant> getAll();
}