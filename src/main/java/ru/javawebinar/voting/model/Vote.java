package ru.javawebinar.voting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javawebinar.voting.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"}, name = "votes_unique_user_date_idx")})
public class Vote extends AbstractBaseEntity {

    @Column(name = "date", nullable = false, columnDefinition = "DATE default now()")
    @NotNull(groups = View.Persist.class)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    public Vote() {
    }

    public Vote(Vote v) {
        this(v.id, v.date, v.user, v.restaurant);
    }

    public Vote(Integer id, LocalDate date) {
        super(id);
        this.date = date;
    }

    public Vote(Integer id, LocalDate date, Restaurant restaurant) {
        this(id, date);
        this.restaurant = restaurant;
    }

    public Vote(Integer id, LocalDate date, User user, Restaurant restaurant) {
        this(id, date, restaurant);
        this.user = user;
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
