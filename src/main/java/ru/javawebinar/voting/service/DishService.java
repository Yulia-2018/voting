package ru.javawebinar.voting.service;

import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.to.DishTo;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface DishService {

    Dish create(Dish dish, int restaurantId);

    //void update(Dish dish, int restaurantId) throws NotFoundException;

    void update(DishTo dish, int restaurantId) throws NotFoundException;

    void delete(int id, int restaurantId) throws NotFoundException;

    Dish get(int id, int restaurantId) throws NotFoundException;

    List<Dish> getAll(int restaurantId, LocalDate date);
}
