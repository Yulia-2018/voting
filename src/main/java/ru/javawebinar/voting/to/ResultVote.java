package ru.javawebinar.voting.to;

import java.time.LocalDate;

public class ResultVote {
    private final Integer id;

    private final String name;

    private final LocalDate date;

    private final int quantity;

    public ResultVote(Integer id, String name, LocalDate date, int quantity) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "ResultVote{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", quantity=" + quantity +
                '}';
    }
}
