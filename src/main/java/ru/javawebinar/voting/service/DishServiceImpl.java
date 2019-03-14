package ru.javawebinar.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.voting.model.Dish;
import ru.javawebinar.voting.repository.DishRepository;
import ru.javawebinar.voting.util.exception.NoAccessException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private final DishRepository repository;

    public DishServiceImpl(DishRepository repository) {
        this.repository = repository;
    }

    @Override
    public Dish create(Dish dish, int restaurantId, int userId) throws NoAccessException {
        Assert.notNull(dish, "dish must not be null");
        return repository.save(dish, restaurantId, userId);
    }

    @Override
    public void update(Dish dish, int restaurantId, int userId) throws NotFoundException, NoAccessException {
        Assert.notNull(dish, "dish must not be null");
        checkNotFoundWithId(repository.save(dish, restaurantId, userId), dish.getId());
    }

    @Override
    public void delete(int id, int restaurantId, int userId) throws NotFoundException, NoAccessException {
        checkNotFoundWithId(repository.delete(id, restaurantId, userId), id);
    }

    @Override
    public Dish get(int id, int restaurantId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    @Override
    public List<Dish> getAll(int restaurantId) {
        return repository.getAll(restaurantId);
    }
}
