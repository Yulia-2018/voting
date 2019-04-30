package ru.javawebinar.voting.to;

import java.time.LocalDate;

public class ResultVote extends BaseTo {

    private String name;

    private LocalDate date;

    private int quantity;

    public ResultVote() {
    }

    public ResultVote(Integer id, String name, LocalDate date, int quantity) {
        super(id);
        this.name = name;
        this.date = date;
        this.quantity = quantity;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultVote that = (ResultVote) o;

        if (quantity != that.quantity) return false;
        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + quantity;
        return result;
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
