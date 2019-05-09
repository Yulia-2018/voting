package ru.javawebinar.voting.to;

import java.time.LocalDate;
import java.util.List;

public class RestaurantsWithDishes {

    private RestaurantTo restaurant;

    private List<DishTo> dishes;

    private LocalDate date;

    public RestaurantsWithDishes() {
    }

    public RestaurantsWithDishes(RestaurantTo restaurant, List<DishTo> dishes, LocalDate date) {
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.date = date;
    }

    public RestaurantTo getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantTo restaurant) {
        this.restaurant = restaurant;
    }

    public List<DishTo> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishTo> dishes) {
        this.dishes = dishes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestaurantsWithDishes that = (RestaurantsWithDishes) o;

        if (!restaurant.equals(that.restaurant)) return false;
        if (dishes != null ? !dishes.equals(that.dishes) : that.dishes != null) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        int result = restaurant.hashCode();
        result = 31 * result + (dishes != null ? dishes.hashCode() : 0);
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RestaurantsWithDishes{" +
                "restaurant=" + restaurant +
                ", dishes=" + dishes +
                ", date=" + date +
                '}';
    }
}
