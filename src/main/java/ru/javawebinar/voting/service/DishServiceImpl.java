package ru.javawebinar.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.repository.DishRepository;
import ru.javawebinar.voting.to.DishTo;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.voting.util.DishUtil.updateFromTo;
import static ru.javawebinar.voting.util.ValidationUtil.*;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private final DishRepository repository;

    public DishServiceImpl(DishRepository repository) {
        this.repository = repository;
    }

    @Override
    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        return repository.save(dish, restaurantId);
    }

    /*@Transactional
    @Override
    public void update(Dish dish, int restaurantId) throws NotFoundException {
        Assert.notNull(dish, "dish must not be null");
        Dish dishInDb = get(dish.getId(), restaurantId);
        checkCurrentDate(dishInDb.getDate());
        repository.save(dish, restaurantId);
    }*/

    @Transactional
    @Override
    public void update(DishTo dishTo, int restaurantId) {
        Dish dishInDb = get(dishTo.getId(), restaurantId);
        checkCurrentDate(dishInDb.getDate());
        Dish dish = updateFromTo(dishInDb, dishTo);
        repository.save(dish, restaurantId);
    }

    @Transactional
    @Override
    public void delete(int id, int restaurantId) throws NotFoundException {
        Dish dishInDb = get(id, restaurantId);
        checkCurrentDate(dishInDb.getDate());
        repository.delete(id, restaurantId);
    }

    @Override
    public Dish get(int id, int restaurantId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    @Override
    public List<Dish> getAll(int restaurantId, LocalDate date) {
        return repository.getAll(restaurantId, date);
    }
}
