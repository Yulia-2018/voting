package ru.javawebinar.voting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NamedQueries({
        @NamedQuery(name = Vote.ALL_SORTED, query = "SELECT v FROM Vote v WHERE v.date=:date ORDER BY v.restaurant.name, v.id")
        /*@NamedQuery(name = Vote.RESULT, query = "SELECT v.date, v.restaurant.id, v.restaurant.name, COUNT(*)" +
                " FROM Vote v" +
                " WHERE v.date=:date" +
                " GROUP BY v.date, v.restaurant.id, v.restaurant.name" +
                " ORDER BY v.restaurant.name")*/
})
@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"}, name = "votes_unique_user_date_idx")})
public class Vote extends AbstractBaseEntity {

    public static final String ALL_SORTED = "Vote.getAllSorted";
    //public static final String RESULT = "Vote.getResult";

    @Column(name = "date", nullable = false, columnDefinition = "DATE default now()")
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    // Поставила EAGER для результатов голосования
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    public Vote() {
    }

    // Подумать, нужны ли во всех конструкторах user и restaurant
    public Vote(Vote v) {
        this(v.id, v.date, v.user, v.restaurant);
    }

    public Vote(Integer id, LocalDate date) {
        super(id);
        this.date = date;
    }

    public Vote(Integer id, LocalDate date, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.restaurant = restaurant;
    }

    public Vote(Integer id, LocalDate date, User user, Restaurant restaurant) {
        this(id, date);
        this.user = user;
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}
