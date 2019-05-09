package ru.javawebinar.voting.to;

import java.time.LocalDate;

public class ResultsVoting {

    private RestaurantTo restaurant;

    private LocalDate date;

    private int quantity;

    public ResultsVoting() {
    }

    public ResultsVoting(RestaurantTo restaurant, LocalDate date, int quantity) {
        this.restaurant = restaurant;
        this.date = date;
        this.quantity = quantity;
    }

    public RestaurantTo getRestaurant() {
        return restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultsVoting that = (ResultsVoting) o;

        if (quantity != that.quantity) return false;
        if (!restaurant.equals(that.restaurant)) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        int result = restaurant.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        return "ResultsVoting{" +
                "restaurant=" + restaurant +
                ", date=" + date +
                ", quantity=" + quantity +
                '}';
    }
}
