package ru.javawebinar.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.repository.RestaurantRepository;
import ru.javawebinar.voting.to.RestaurantTo;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.voting.util.RestaurantUtil.updateFromTo;
import static ru.javawebinar.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private final RestaurantRepository repository;

    public RestaurantServiceImpl(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    @Transactional
    @Override
    public void update(RestaurantTo restaurantTo) {
        Restaurant restaurant = updateFromTo(get(restaurantTo.getId()), restaurantTo);
        checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
    }

    @Override
    public Restaurant get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Restaurant> getAllWithDishes(LocalDate date) {
        return repository.getAllWithDishes(date);
    }
}
