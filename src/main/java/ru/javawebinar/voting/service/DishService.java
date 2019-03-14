package ru.javawebinar.voting.service;

import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.util.exception.NoAccessException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.util.List;

public interface DishService {

    Dish create(Dish dish, int restaurantId, int userId) throws NoAccessException;

    void update(Dish dish, int restaurantId, int userId) throws NotFoundException, NoAccessException;

    void delete(int id, int restaurantId, int userId) throws NotFoundException, NoAccessException;

    Dish get(int id, int restaurantId) throws NotFoundException;

    List<Dish> getAll(int restaurantId);
}
