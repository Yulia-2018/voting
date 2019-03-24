package ru.javawebinar.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.repository.RestaurantRepository;
import ru.javawebinar.voting.util.exception.NoAccessException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private final RestaurantRepository repository;

    public RestaurantServiceImpl(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant create(Restaurant restaurant, int userId) throws NoAccessException {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant, userId);
    }

    @Override
    public void update(Restaurant restaurant, int userId) throws NoAccessException {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFoundWithId(repository.save(restaurant, userId), restaurant.getId());
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException, NoAccessException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Restaurant get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public List<Restaurant> getAll() {
        return repository.getAll();
    }
}