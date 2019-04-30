package ru.javawebinar.voting.util;


import ru.javawebinar.voting.HasId;
import ru.javawebinar.voting.util.exception.InvalidDateTimeException;
import ru.javawebinar.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalArgumentException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalArgumentException(bean + " must be with id=" + id);
        }
    }

    public static void checkInvalidDateTime(LocalDate date, LocalTime time) {
        if (date.compareTo(LocalDate.now()) != 0 | time.compareTo(LocalTime.of(11, 0)) > 0) {
            throw new InvalidDateTimeException("date " + date + " or time " + time + " is invalid");
        }
    }
}