package ru.javawebinar.voting.util;

import ru.javawebinar.voting.model.Restaurant;
import ru.javawebinar.voting.to.RestaurantTo;

public class RestaurantUtil {

    private RestaurantUtil() {
    }

    public static Restaurant createNewFromTo(RestaurantTo newRestaurant) {
        return new Restaurant(null, newRestaurant.getName());
    }

    public static Restaurant updateFromTo(Restaurant restaurant, RestaurantTo restaurantTo) {
        restaurant.setName(restaurantTo.getName());
        return restaurant;
    }

    public static RestaurantTo asTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName());
    }
}
