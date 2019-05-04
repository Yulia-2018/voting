package ru.javawebinar.voting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NamedQueries({
        @NamedQuery(name = Dish.DELETE, query = "DELETE FROM Dish m WHERE m.id=:id AND m.restaurant.id=:restaurantId"),
        @NamedQuery(name = Dish.ALL_SORTED, query = "SELECT m FROM Dish m WHERE m.restaurant.id=:restaurantId AND m.date=:date ORDER BY m.name")
})
@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "date", "name"}, name = "dishes_unique_restaurant_date_name_idx")})
public class Dish extends AbstractNamedEntity {

    public static final String DELETE = "Dish.delete";
    public static final String ALL_SORTED = "Dish.getAllSorted";

    @Column(name = "price", nullable = false)
    @Range(min = 1, max = 100000)
    @NotNull
    private Integer price;

    @Column(name = "date", nullable = false, columnDefinition = "DATE default now()")
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(Dish m) {
        this(m.id, m.name, m.price, m.date, m.restaurant);
    }

    public Dish(Integer id, String name, Integer price, LocalDate date) {
        super(id, name);
        this.price = price;
        this.date = date;
    }

    public Dish(Integer id, String name, Integer price, LocalDate date, Restaurant restaurant) {
        this(id, name, price, date);
        this.restaurant = restaurant;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
